package bg.sofia.uni.fmi.mjt.ethanolchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class OutputClient implements Runnable {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;
    private final String host;
    private final int port;
    private final String username;
    private final String code;

    public OutputClient(String host, int port, String username, String code) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.code = code;
    }

    @Override
    public void run() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true)) {

            socketChannel.connect(new InetSocketAddress(host, port));
            writer.println(Common.makeCredentials(username, code, true));

            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                if (!message.isBlank()) {
                    System.out.println(message);
                }
            }

        } catch (IOException e) {
            System.out.println("There's a problem with the network communication");
        }
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        String code = Common.getInput(Common.ENTER_CODE, console);
        String username = Common.getInput(Common.ENTER_USERNAME, console);
        Common.clearConsole();

        OutputClient client = new OutputClient(DEFAULT_HOST, DEFAULT_PORT, username, code);
        client.run();
    }
}