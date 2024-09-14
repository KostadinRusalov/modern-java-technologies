package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveFromWishListCommandTest {
    String cannotRemove = "Sorry, cannot remove the drink.";
    @Mock
    TheCocktailDB theCocktailDB;

    @Mock
    Chat chat;

    @Test
    void testRemoveFromWishListCommandBlankQuery() {
        assertEquals(cannotRemove, new RemoveFromWishListCommand("", chat).execute());
    }

    @Test
    void testRemoveFromWishListCommandNoResponse() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("soup")).thenReturn(null);

        assertEquals(cannotRemove, new RemoveFromWishListCommand("soup", chat).execute());
    }

    @Test
    void testRemoveFromWishListCommandNoDrinks() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("soup")).thenReturn(Common.nullResponse);

        assertEquals(cannotRemove, new RemoveFromWishListCommand("soup", chat).execute());
    }

    @Test
    void testRemoveFromWishListNoWishlist() {
        String noWishlist = "Sorry, there is no wishlist.";
        Map<ChatId, WishList> wishlist = new HashMap<>();
        AbstractBartenderWishListCommand.configureWishlistStorage(wishlist);

        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        when(chat.getChatId()).thenReturn(new ChatId("test", LocalDateTime.now()));

        assertEquals(noWishlist, new RemoveFromWishListCommand("whiskey", chat).execute());
    }

    @Test
    void testRemoveFromWishListNoDrinkInWishlist() {
        String noDrink = "Hey, there was no such drink in the wishlist.";
        Map<ChatId, WishList> wishlist = new HashMap<>();
        ChatId chatId = new ChatId("test", LocalDateTime.now());
        wishlist.put(chatId, new WishList());

        AbstractBartenderWishListCommand.configureWishlistStorage(wishlist);

        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        when(chat.getChatId()).thenReturn(chatId);

        assertEquals(noDrink, new RemoveFromWishListCommand("whiskey", chat).execute());
    }

    @Test
    void testRemoveFromWishListDrinkInWishlist() {
        String drink = "Hey, the drink was just removed from the wishlist.";
        Map<ChatId, WishList> wishlist = new HashMap<>();
        ChatId chatId = new ChatId("test", LocalDateTime.now());

        wishlist.put(chatId, new WishList(new HashSet<>(List.of(Common.whiskey))));

        AbstractBartenderWishListCommand.configureWishlistStorage(wishlist);

        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        when(chat.getChatId()).thenReturn(chatId);

        assertEquals(drink, new RemoveFromWishListCommand("whiskey", chat).execute());
    }

    @Test
    void testExecuteThrows() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("vodka"))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequestException, new RemoveFromWishListCommand("vodka", chat).execute());
    }
}
