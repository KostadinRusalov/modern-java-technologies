package bg.sofia.uni.fmi.mjt.ethanolchat.server.exception;

public class InactiveChatException extends RuntimeException {
    public InactiveChatException(String message) {
        super(message);
    }

    public InactiveChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
