package bg.sofia.uni.fmi.mjt.space.exception;

import java.util.function.Function;

public class Requirements {
    public static void requireNotNull(Object object, String message) {
        requireNotNull(object, message, IllegalArgumentException::new);
    }

    public static void requireNotNull(Object object, String message,
                                      Function<String, ? extends RuntimeException> exceptionConstructor) {
        requireTrue(object != null, message, exceptionConstructor);
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
