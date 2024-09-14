package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommandFactory;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.UserCredentials;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.MessageFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.ActiveChatRepository;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.InactiveChatRepository;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.InactiveChatException;

import java.time.LocalDateTime;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.Requirement.requireFalse;
import static bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.Requirement.requireNotNull;

public class ChatManager {
    private ActiveChatRepository activeChats;
    private InactiveChatRepository inactiveChats;
    private MessageFormatter messageFormatter;
    private Set<BotCommandFactory> botCommandFactories;
    private Logger logger;

    public ChatManager(ActiveChatRepository activeChats,
                       InactiveChatRepository inactiveChats,
                       MessageFormatter messageFormatter,
                       Set<BotCommandFactory> botCommandFactories,
                       Logger logger) {
        this.activeChats = activeChats;
        this.inactiveChats = inactiveChats;
        this.messageFormatter = messageFormatter;
        this.botCommandFactories = botCommandFactories;
        this.logger = logger;
    }

    public Chat getChat(String chat) {
        return activeChats.read(chat);
    }

    public Chat joinChat(UserCredentials credentials) {
        requireNotNull(credentials, "Credentials must not be null");

        Chat chat = activeChats.read(credentials.chat());
        if (chat != null) {
            logger.log("New member joined chat: " + credentials.chat());
            chat.addMember(credentials.username());
            chat.sentMessage(messageFormatter.joinChat(credentials));
            activeChats.update(chat);
        } else {
            logger.log("New active chat created: " + credentials.chat());
            chat = new Chat(credentials.chat(), credentials.username(), LocalDateTime.now(), botCommandFactories);
            chat.sentMessage(messageFormatter.createChat(credentials));
            activeChats.create(chat);
        }
        return chat;
    }

    public void leaveChat(UserCredentials credentials) {
        requireNotNull(credentials, "Credentials must not be null");

        Chat chat = activeChats.read(credentials.chat());
        requireNotNull(chat, "There is no active chat " + credentials.chat(), InactiveChatException::new);

        chat.sentMessage(messageFormatter.leaveChat(credentials));
        chat.removeMember(credentials.username());
        activeChats.update(chat);

        logger.log("Member left the chat: " + chat.getCode());
        if (chat.getActiveMembers().isEmpty()) {
            logger.log("Closed active chat: " + chat.getCode());
            activeChats.delete(chat.getCode());
            inactiveChats.create(chat);
        }
    }

    public void sentMessage(UserCredentials credentials, String message) {
        requireNotNull(credentials, "Credentials must not be null");
        requireNotNull(message, "Message must not be null");
        requireFalse(message.isBlank(), "Message must not be blank");

        Chat chat = activeChats.read(credentials.chat());
        requireNotNull(chat, "There is no active chat " + credentials.chat(), InactiveChatException::new);

        chat.sentMessage(messageFormatter.sentMessage(credentials.username(), message));
        executeIfBotCommand(message, chat);
        activeChats.update(chat);
    }

    private void executeIfBotCommand(String message, Chat chat) {
        for (BotCommandFactory bot : chat.getBotCommandFactories()) {
            if (message.startsWith(bot.getCommandPrefix())) {
                Thread.ofVirtual().start(new BotCommandExecutor(message, bot, chat, messageFormatter));
            }
        }
    }
}
