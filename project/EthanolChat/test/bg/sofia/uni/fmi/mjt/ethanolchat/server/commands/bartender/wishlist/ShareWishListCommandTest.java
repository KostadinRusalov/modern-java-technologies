package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile.GoFile;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.DrinkFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShareWishListCommandTest {
    @Mock
    GoFile goFile;

    @Mock
    DrinkFormatter drinkFormatter;

    @InjectMocks
    ShareWishListCommand command;
    @Mock
    static Map<ChatId, WishList> wishlists;

    @Mock
    Chat chat;

    ChatId chatId = new ChatId("test", LocalDateTime.parse("2027-12-03T10:15:30"));

    @BeforeEach
    void setUp() {
        AbstractBartenderWishListCommand.configureWishlistStorage(wishlists);
        when(chat.getChatId()).thenReturn(chatId);
    }

    @Test
    void testExecute() {
        WishList wishList = new WishList(Set.of(Common.whiskey));

        when(wishlists.get(chatId)).thenReturn(wishList);
        when(goFile.getServer())
            .thenReturn(new GoFile.Response("ok", new GoFile.Data("server", null)));

        when(goFile.uploadFile(anyString(), any(Path.class)))
            .thenReturn(new GoFile.Response("ok", new GoFile.Data(null, "link")));

        assertEquals("Here's the wishlist: link", command.execute());
    }

    @Test
    void testExecuteFailed() {
        String message = "Sorry, there's a problem with sharing the wishlist.";
        WishList wishList = new WishList(Set.of(Common.whiskey));

        when(wishlists.get(chatId)).thenReturn(wishList);

        when(goFile.getServer()).thenThrow(UnsuccessfulRequestException.class);

        assertEquals(message, command.execute());
    }

    @Test
    void testExecuteNoWishList() {
        assertEquals("Sorry, there is no wishlist.", command.execute());
    }
}
