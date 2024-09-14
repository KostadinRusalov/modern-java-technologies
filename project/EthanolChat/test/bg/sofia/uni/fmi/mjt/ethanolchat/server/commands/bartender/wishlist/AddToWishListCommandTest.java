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
public class AddToWishListCommandTest {
    String cannotAdd = "Sorry, there is no drink to add.";

    @Mock
    TheCocktailDB theCocktailDB;

    @Mock
    Chat chat;

    @Test
    void testAddToWishListCommandBlankQuery() {
        assertEquals(cannotAdd, new AddToWishListCommand("", chat).execute());
    }

    @Test
    void testAddToWishListCommandNoResponse() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("soup")).thenReturn(null);

        assertEquals(cannotAdd, new AddToWishListCommand("soup", chat).execute());
    }

    @Test
    void testAddToWishListCommandNoDrinks() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("soup")).thenReturn(Common.nullResponse);

        assertEquals(cannotAdd, new AddToWishListCommand("soup", chat).execute());
    }

    @Test
    void testAddToWishlistCommandFirstDrink() {
        ChatId chatId = new ChatId("test", LocalDateTime.now());

        Map<ChatId, WishList> wishLists = new HashMap<>();
        wishLists.put(chatId, new WishList());

        AbstractBartenderWishListCommand.configureWishlistStorage(wishLists);
        when(chat.getChatId()).thenReturn(chatId);

        String addedToWishlist = "Hey, the drink is added to the wishlist.";

        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        assertEquals(addedToWishlist, new AddToWishListCommand("whiskey", chat).execute());
    }

    @Test
    void testAddToWishlistCommandAlreadyInDrink() {
        ChatId chatId = new ChatId("test", LocalDateTime.now());

        Map<ChatId, WishList> wishLists = new HashMap<>();
        wishLists.put(chatId, new WishList(new HashSet<>(List.of(Common.whiskey))));

        AbstractBartenderWishListCommand.configureWishlistStorage(wishLists);
        when(chat.getChatId()).thenReturn(chatId);

        String addedToWishlist ="Hey, the drinks is already added to the wishlist.";

        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        assertEquals(addedToWishlist, new AddToWishListCommand("whiskey", chat).execute());
    }

    @Test
    void testExecuteThrows() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("vodka"))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequestException, new AddToWishListCommand("vodka", chat).execute());
    }
}
