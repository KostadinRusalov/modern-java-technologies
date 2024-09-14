package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommandFactory;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.InvalidCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.MessageFormatter;

public class BotCommandExecutor implements Runnable {
    private final String commandMessage;
    private final BotCommandFactory botCommandFactory;
    private final Chat chat;
    private final MessageFormatter messageFormatter;

    public BotCommandExecutor(String commandMessage, BotCommandFactory botCommandFactory,
                              Chat chat, MessageFormatter messageFormatter) {
        this.commandMessage = commandMessage;
        this.botCommandFactory = botCommandFactory;
        this.chat = chat;
        this.messageFormatter = messageFormatter;
    }

    @Override
    public void run() {
        BotCommand command = botCommandFactory.parse(commandMessage, chat);
        if (command instanceof InvalidCommand) {
            chat.sentMessage(command.execute());
        } else {
            chat.sentMessage(messageFormatter.sentMessage(command.botName(), command.execute()));
        }
    }
}
