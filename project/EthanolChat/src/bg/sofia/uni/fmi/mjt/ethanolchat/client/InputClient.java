package bg.sofia.uni.fmi.mjt.ethanolchat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;
    private final String host;
    private final int port;
    private final String username;
    private final String code;

    public InputClient(String host, int port, String username, String code) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.code = code;
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner console = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(host, port));
            writer.println(Common.makeCredentials(username, code, false));
            List<String> helpCommands = readHelpCommands(socketChannel);

            while (true) {
                Common.clearConsole();
                String message = console.nextLine().strip();
                if (message.isBlank()) {
                    continue;
                }
                if ("/leave".equals(message)) {
                    break;
                }

                if ("/help".equals(message)) {
                    helpCommands.forEach(System.out::println);
                    continue;
                }
                writer.println(message);
            }
        } catch (IOException e) {
            System.out.println("There's a problem with the network communication.");
        }
    }

    private List<String> readHelpCommands(SocketChannel socketChannel) {
        Scanner scanner = new Scanner(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
        List<String> commands = new ArrayList<>();

        String command = scanner.nextLine();
        while (!"END".equals(command)) {
            commands.add(command);
            command = scanner.nextLine();
        }
        return commands;
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        String code = Common.getInput(Common.ENTER_CODE, console);
        String username = Common.getInput(Common.ENTER_USERNAME, console);
        Common.clearConsole();

        new InputClient(DEFAULT_HOST, DEFAULT_PORT, username, code).start();
    }
}