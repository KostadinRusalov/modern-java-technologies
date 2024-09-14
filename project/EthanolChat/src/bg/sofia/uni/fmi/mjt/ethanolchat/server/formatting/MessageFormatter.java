package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.UserCredentials;

public interface MessageFormatter {
    String createChat(UserCredentials credentials);

    String joinChat(UserCredentials credentials);

    String leaveChat(UserCredentials credentials);

    String sentMessage(String user, String message);
}
