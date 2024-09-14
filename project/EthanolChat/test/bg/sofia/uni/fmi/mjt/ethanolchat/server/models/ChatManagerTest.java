package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommandFactory;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.credentials.UserCredentials;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.InactiveChatException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.MessageFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.ActiveChatRepository;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.InactiveChatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatManagerTest {
    @Mock
    ActiveChatRepository activeChatRepository;
    @Mock
    InactiveChatRepository inactiveChatRepository;
    @Mock
    MessageFormatter messageFormatter;
    @Mock
    Set<BotCommandFactory> botCommandFactorySet;
    @Mock
    Logger logger;
    @InjectMocks
    ChatManager chatManager;

    @Mock
    Chat chat;

    UserCredentials credentials = new UserCredentials("whiskey", "test");

    @Test
    void testJoinChatNullCredentialsThrows() {
        assertThrows(IllegalArgumentException.class, () -> chatManager.joinChat(null));
    }

    @Test
    void testLeaveChatNullCredentialsThrows() {
        assertThrows(IllegalArgumentException.class, () -> chatManager.leaveChat(null));
    }

    @Test
    void testSentMessageNullCredentials() {
        assertThrows(IllegalArgumentException.class, () -> chatManager.sentMessage(null, null));
    }

    @Test
    void testSentMessageNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> chatManager.sentMessage(credentials, null));
    }

    @Test
    void testSentMessageEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> chatManager.sentMessage(credentials, ""));
    }

    @Test
    void testJoinChatCreateChat() {
        when(messageFormatter.createChat(credentials)).thenReturn("whiskey created the chat test");

        Chat chat = chatManager.joinChat(credentials);
        assertEquals("test", chat.getCode());
        assertEquals("whiskey", chat.getCreatedBy());
        assertEquals(List.of("whiskey created the chat test"), chat.getMessages());
        assertEquals(Set.of("whiskey"), chat.getActiveMembers());

        verify(activeChatRepository).create(chat);
    }

    @Test
    void testJoinChatExistingChat() {
        when(activeChatRepository.read(credentials.chat())).thenReturn(chat);
        when(messageFormatter.joinChat(credentials)).thenReturn("whiskey joined the chat");

        Chat chat = chatManager.joinChat(credentials);

        verify(chat).addMember("whiskey");
        verify(chat).sentMessage("whiskey joined the chat");
        verify(activeChatRepository, times(1)).update(chat);
    }

    @Test
    void testLeaveChatNoChat() {
        assertThrows(InactiveChatException.class, () -> chatManager.leaveChat(credentials));
    }

    @Test
    void testLeaveChatNotLastMember() {
        when(activeChatRepository.read(credentials.chat())).thenReturn(chat);
        when(chat.getActiveMembers()).thenReturn(Set.of("vodka"));
        when(messageFormatter.leaveChat(credentials)).thenReturn("whiskey left");

        chatManager.leaveChat(credentials);

        verify(chat).sentMessage("whiskey left");
        verify(chat).removeMember("whiskey");
        verify(activeChatRepository).update(chat);
    }

    @Test
    void testLeaveChatLastMember() {
        when(activeChatRepository.read(credentials.chat())).thenReturn(chat);
        when(chat.getActiveMembers()).thenReturn(Collections.emptySet());
        when(chat.getCode()).thenReturn(credentials.chat());
        chatManager.leaveChat(credentials);

        verify(activeChatRepository).delete(credentials.chat());
        verify(inactiveChatRepository).create(chat);
    }

    @Test
    void testSentMessageNoChat() {
        assertThrows(InactiveChatException.class, () -> chatManager.sentMessage(credentials, "rum"));
    }

    @Test
    void testSentMessage() {
        when(activeChatRepository.read(credentials.chat())).thenReturn(chat);
        when(messageFormatter.sentMessage(credentials.username(), "bye bye")).thenReturn("bye bye");

        chatManager.sentMessage(credentials, "bye bye");

        verify(chat).sentMessage("bye bye");
        verify(activeChatRepository).update(chat);
    }
}
