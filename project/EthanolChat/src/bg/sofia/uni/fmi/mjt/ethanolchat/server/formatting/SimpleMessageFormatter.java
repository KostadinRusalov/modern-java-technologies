package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.UserCredentials;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleMessageFormatter implements MessageFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String createChat(UserCredentials credentials) {
        return STR. "\{ credentials.username() } created the chat \{ credentials.chat() }" ;
    }

    @Override
    public String joinChat(UserCredentials credentials) {
        return STR. "\{ credentials.username() } joined the chat \{ credentials.chat() }" ;
    }

    @Override
    public String leaveChat(UserCredentials credentials) {
        return STR. "\{ credentials.username() } left the chat \{ credentials.chat() }" ;
    }

    @Override
    public String sentMessage(String user, String message) {
        return STR. "[\{ TIME_FORMATTER.format(LocalDateTime.now()) }] \{ user }: \{ message }" ;
    }
}
