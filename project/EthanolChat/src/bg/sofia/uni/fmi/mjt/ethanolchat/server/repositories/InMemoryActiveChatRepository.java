package bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryActiveChatRepository implements ActiveChatRepository {
    private final Map<String, Chat> activeChats = new HashMap<>();

    @Override
    public void create(Chat chat) {
        activeChats.put(chat.getCode(), chat);
    }

    @Override
    public Chat read(String key) {
        return activeChats.get(key);
    }

    @Override
    public Collection<Chat> readAll() {
        return activeChats.values();
    }

    @Override
    public void update(Chat chat) {
        activeChats.replace(chat.getCode(), chat);
    }

    @Override
    public void delete(String key) {
        activeChats.remove(key);
    }
}
