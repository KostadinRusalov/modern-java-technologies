package bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials;

public class ChatCredentials {
    private final UserCredentials userCredentials;
    private int lastReadMessageId;

    public ChatCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getChat() {
        return userCredentials.chat();
    }

    public String getUsername() {
        return userCredentials.username();
    }

    public int getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(int lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
