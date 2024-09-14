package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShowWishListCommandTest {
    String noWishlist = "Sorry, there is no wishlist.";

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
    void testExecuteNullWishlist() {
        when(wishlists.get(chatId)).thenReturn(null);

        assertEquals(noWishlist, new ShowWishListCommand(chat).execute());
    }

    @Test
    void testExecuteEmptyWishlist() {
        WishList wishList = new WishList();
        when(wishlists.get(chatId)).thenReturn(wishList);

        assertEquals(noWishlist, new ShowWishListCommand(chat).execute());
    }

    @Test
    void testExecuteWithDrinks() {
        String response = "Drinks in wishlist: vodka";
        WishList wishList = new WishList(Set.of(Common.vodka));
        when(wishlists.get(chatId)).thenReturn(wishList);

        assertEquals(response, new ShowWishListCommand(chat).execute());
    }
}
