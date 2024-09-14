package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands;

public class InvalidCommand implements BotCommand {
    private final String botName;
    private final String message;

    public InvalidCommand() {
        this("Invalid bot!", "Invalid command!");
    }

    public InvalidCommand(String botName, String message) {
        this.botName = botName;
        this.message = message;
    }

    @Override
    public String botName() {
        return botName;
    }

    @Override
    public String execute() {
        return message;
    }
}
