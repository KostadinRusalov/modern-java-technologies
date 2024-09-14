package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommandFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chat {
    private String code;
    private LocalDateTime createdOn;
    private ChatId chatId;
    private String createdBy;
    private List<String> messages;
    private Set<String> activeMembers;
    private Set<BotCommandFactory> botCommandFactories;

    public Chat(String code, String creator, LocalDateTime createdOn) {
        this(code, creator, createdOn, new HashSet<>());
    }

    public Chat(String code, String creator, LocalDateTime createdOn, Set<BotCommandFactory> botCommandFactories) {
        this.code = code;
        this.createdOn = createdOn;
        this.chatId = new ChatId(code, createdOn);
        this.createdBy = creator;
        this.messages = new ArrayList<>();
        this.activeMembers = new HashSet<>();
        this.activeMembers.add(creator);
        this.botCommandFactories = botCommandFactories;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Set<String> getActiveMembers() {
        return activeMembers;
    }

    public Set<BotCommandFactory> getBotCommandFactories() {
        return botCommandFactories;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setBotCommandFactories(Set<BotCommandFactory> botCommandFactories) {
        this.botCommandFactories = botCommandFactories;
    }

    public synchronized void sentMessage(String message) {
        messages.add(message);
    }

    public boolean addMember(String member) {
        return activeMembers.add(member);
    }

    public boolean removeMember(String member) {
        return activeMembers.remove(member);
    }
}