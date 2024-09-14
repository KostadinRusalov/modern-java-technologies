package bg.sofia.uni.fmi.mjt.ethanolchat.server.exception;

public class UnsuccessfulRequestException extends RuntimeException {
    public UnsuccessfulRequestException(String message) {
        super(message);
    }

    public UnsuccessfulRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
