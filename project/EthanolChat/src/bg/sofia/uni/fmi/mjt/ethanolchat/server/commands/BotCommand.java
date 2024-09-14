package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands;

/**
 * Interface that represents a console command for a bot.
 */
public interface BotCommand {
    /**
     * @return the name of the bot
     */
    String botName();

    /**
     * Executes the command
     * @return the result of the execution
     */
    String execute();
}
