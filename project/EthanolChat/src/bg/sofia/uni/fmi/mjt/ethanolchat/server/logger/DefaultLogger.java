package bg.sofia.uni.fmi.mjt.ethanolchat.server.logger;

import java.time.LocalDateTime;

public class DefaultLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println(STR. "\{ LocalDateTime.now() } [INFO] \{ message }" );
    }
}
