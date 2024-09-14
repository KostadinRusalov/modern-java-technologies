package bg.sofia.uni.fmi.mjt.ethanolchat.server.exception;

import java.util.function.Function;

public class Requirement {
    public static void requireNotNull(Object object, String message) {
        requireNotNull(object, message, IllegalArgumentException::new);
    }

    public static void requireNotNull(Object object, String message,
                                      Function<String, ? extends RuntimeException> exceptionConstructor) {
        requireTrue(object != null, message, exceptionConstructor);
    }

    public static void requireFalse(boolean condition, String message) {
        requireTrue(!condition, message);
    }

    public static void requireFalse(boolean condition, String message,
                                    Function<String, ? extends RuntimeException> exceptionConstructor) {
        requireTrue(!condition, message, exceptionConstructor);
    }

    public static void requireTrue(boolean condition, String message) {
        requireTrue(condition, message, IllegalArgumentException::new);
    }

    public static void requireTrue(boolean condition, String message,
                                   Function<String, ? extends RuntimeException> exceptionConstructor) {
        if (!condition) {
            throw exceptionConstructor.apply(message);
        }
    }
}
