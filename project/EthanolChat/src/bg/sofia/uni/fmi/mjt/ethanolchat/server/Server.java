package bg.sofia.uni.fmi.mjt.ethanolchat.server;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.ChatCredentials;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.UserCredentials;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import java.util.Iterator;
import java.util.List;

public class Server {
    private static final String CREDENTIALS_DELIMITER = "\\|";
    private static final int CLIENT_TYPE = 0;
    private static final int USERNAME = 1;
    private static final int CHAT_CODE = 2;
    private static final String CHAT = "1";

    private final String host;
    private final int port;
    private final ChatManager chatManager;
    private boolean isWorking;
    private Selector selector;

    private Logger logger;

    public Server(String host, int port, ChatManager chatManager, Logger logger) {
        this.host = host;
        this.port = port;
        this.chatManager = chatManager;
        this.logger = logger;
    }

    public void start() {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            logger.log("Server started");
            selector = Selector.open();
            configureServer(server);

            isWorking = true;
            while (isWorking) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isAcceptable()) {
                        handleAcceptable(key);
                    } else if (key.isReadable()) {
                        handleReadable(key);
                    } else if (key.isWritable()) {
                        handleWriteable(key);
                    }
                    keys.remove();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Internal server exception", e);
        }
    }

    public void stop() {
        isWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServer(ServerSocketChannel server) throws IOException {
        server.bind(new InetSocketAddress(host, port))
            .configureBlocking(false)
            .register(selector, SelectionKey.OP_ACCEPT);
    }

    private void handleAcceptable(SelectionKey key) throws IOException {
        logger.log("Client connection accepted");

        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        server.accept()
            .configureBlocking(false)
            .register(selector, SelectionKey.OP_READ);
    }

    private void handleReadable(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        BufferedReader reader = new BufferedReader(Channels.newReader(client, StandardCharsets.UTF_8));

        String message = reader.readLine();
        if (message == null) {
            chatManager.leaveChat((UserCredentials) key.attachment());
            client.close();
            logger.log("Input client connection closed");
            return;
        }

        if (key.attachment() != null) {
            chatManager.sentMessage((UserCredentials) key.attachment(), message);
        } else {
            authorizeClient(key, message);
        }
    }

    private void authorizeClient(SelectionKey key, String message) throws IOException {
        Object attachment = getCredentials(message);
        key.attach(attachment);

        if (attachment instanceof UserCredentials credentials) {
            Chat chat = chatManager.joinChat(credentials);
            sendHelpCommands(key, chat);
            return;
        }

        ChatCredentials credentials = (ChatCredentials) attachment;
        Chat chat = chatManager.getChat(credentials.getChat());

        if (chat != null && chat.getActiveMembers().contains(credentials.getUsername())) {
            key.interestOps(SelectionKey.OP_WRITE);
        } else {
            SocketChannel client = (SocketChannel) key.channel();
            PrintWriter writer = new PrintWriter(Channels.newWriter(client, StandardCharsets.UTF_8), true);
            writer.println("Invalid user credentials!");
            client.close();
            logger.log("Unsuccessful attempt to join a chat client.");
        }
    }

    private void sendHelpCommands(SelectionKey key, Chat chat) {
        SocketChannel client = (SocketChannel) key.channel();
        PrintWriter writer = new PrintWriter(Channels.newWriter(client, StandardCharsets.UTF_8), true);
        for (var bot : chat.getBotCommandFactories()) {
            for (var command : bot.getCommands()) {
                writer.println(command);
            }
        }
        writer.println("END");
    }

    private void handleWriteable(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ChatCredentials credentials = (ChatCredentials) key.attachment();
        PrintWriter writer = new PrintWriter(Channels.newWriter(client, StandardCharsets.UTF_8), true);

        Chat chat = chatManager.getChat(credentials.getChat());
        if (chat == null || !chat.getActiveMembers().contains(credentials.getUsername())) {
            client.close();
            logger.log("Output client connection closed");
            return;
        }

        List<String> messages = chat.getMessages();
        int messageId = credentials.getLastReadMessageId();

        while (messageId < messages.size()) {
            String message = messages.get(messageId++);
            writer.println(message);
        }
        credentials.setLastReadMessageId(messageId);
    }

    private static Object getCredentials(String credentials) {
        String[] tokens = credentials.split(CREDENTIALS_DELIMITER);
        UserCredentials userCredentials = new UserCredentials(tokens[USERNAME], tokens[CHAT_CODE]);
        return tokens[CLIENT_TYPE].equals(CHAT) ? new ChatCredentials(userCredentials) : userCredentials;
    }
}