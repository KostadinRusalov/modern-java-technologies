package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;

import java.util.List;

public interface BotCommandFactory {
    /**
     * @return the command prefix of the bot
     */
    String getCommandPrefix();

    /**
     * Changes the command prefix.
     *
     * @param prefix the new prefix
     * @return the old prefix
     */
    String changeCommandPrefix(String prefix);

    /**
     * Tries to parse the command
     *
     * @param input the command starting with the prefix
     * @return the command of the bot or InvalidCommand if that bot has no such command
     */
    BotCommand parse(String input, Chat chat);

    /**
     *
     * @return a list with all commands in the format [command] [args] - [description]
     */
    List<String> getCommands();
}
