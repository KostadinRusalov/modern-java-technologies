package bg.sofia.uni.fmi.mjt.cookingcompass.exception;

public class EdamamClientRequestException extends RuntimeException {
    public EdamamClientRequestException(String message) {
        super(message);
    }

    public EdamamClientRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
