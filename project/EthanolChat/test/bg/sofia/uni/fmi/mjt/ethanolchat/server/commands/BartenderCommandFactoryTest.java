package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.CocktailInfoCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.RandomCocktailCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.SearchCocktailByFirstLetterCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.SearchIngredientCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByAlcoholicCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByCategoryCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByGlassCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByIngredientCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.SearchCocktailByNameCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.AddToWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.RemoveFromWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.ShareWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.ShowWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
public class BartenderCommandFactoryTest {
    BartenderCommandFactory bartenderCommandFactory = new BartenderCommandFactory("/");

    Chat chat = new Chat("test", "me", LocalDateTime.now());
    @Mock
    static TheCocktailDB theCocktailDB;

    @Test
    void testGetCommands() {
        assertEquals(List.of(
            "/search [name] - searches for drinks with that name",
            "/info [name] - shows info about the drink",
            "/random - shows info about random drink",
            "/search-by-letter [letter] - searches for drinks with that letter",
            "/search-ingredient [name] - shows info about that ingredient",
            "/filter-by-ingredient [ingredient] - filters drinks with that ingredient",
            "/filter-by-alcoholic [alcoholic/non alcoholic/optional alcohol] - filters by type",
            "/filter-by-category [category] - filters by category",
            "/filter-by-glass [glass] - filters by glass",
            "/add-to-wishlist [drink] - add drink to wishlist",
            "/remove-from-wishlist [drink] - removes from wishlist",
            "/show-wishlist - shows drinks in wishlist",
            "/share-wishlist shares a file with the drinks from the wishlist"
        ), bartenderCommandFactory.getCommands());
    }

    @Test
    void testParseSearchByNameCommand() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);

        assertInstanceOf(SearchCocktailByNameCommand.class,
            bartenderCommandFactory.parse("/search whiskey sour", chat));
    }

    @Test
    void testParseSearchIngredientCommand() {
        assertInstanceOf(SearchIngredientCommand.class,
            bartenderCommandFactory.parse("/search-ingredient vodka", chat));
    }

    @Test
    void testParseSearchByFirstLetterCommand() {
        assertInstanceOf(SearchCocktailByFirstLetterCommand.class,
            bartenderCommandFactory.parse("/search-by-letter f", chat));
    }

    @Test
    void testParseCocktailInfoCommand() {
        assertInstanceOf(CocktailInfoCommand.class,
            bartenderCommandFactory.parse("/info vodka", chat));
    }

    @Test
    void testParseRandomCocktailCommand() {
        assertInstanceOf(RandomCocktailCommand.class,
            bartenderCommandFactory.parse("/random", chat));
    }

    @Test
    void testParseRandomCocktailMustNotHaveArgsCommand() {
        assertInstanceOf(InvalidCommand.class,
            bartenderCommandFactory.parse("/random cocktail", chat));
    }

    @Test
    void testParseFilterByIngredientCommand() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);

        assertInstanceOf(FilterByIngredientCommand.class,
            bartenderCommandFactory.parse("/filter-by-ingredient vodka", chat));
    }

    @Test
    void testParseFilterByAlcoholicCommand() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);

        assertInstanceOf(FilterByAlcoholicCommand.class,
            bartenderCommandFactory.parse("/filter-by-alcoholic non alcoholic", chat));
    }

    @Test
    void testParseFilterByCategoryCommand() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);

        assertInstanceOf(FilterByCategoryCommand.class,
            bartenderCommandFactory.parse("/filter-by-category ordinary drinks", chat));
    }

    @Test
    void testParseFilterByGlassCommand() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);

        assertInstanceOf(FilterByGlassCommand.class,
            bartenderCommandFactory.parse("/filter-by-glass jar", chat));
    }

    @Test
    void testParseAddToWishListCommand() {
        assertInstanceOf(AddToWishListCommand.class,
            bartenderCommandFactory.parse("/add-to-wishlist tequila", chat));
    }

    @Test
    void testParseRemoveFromWishlistCommand() {
        assertInstanceOf(RemoveFromWishListCommand.class,
            bartenderCommandFactory.parse("/remove-from-wishlist martini", chat));
    }

    @Test
    void testParseShowWishlistCommand() {
        assertInstanceOf(ShowWishListCommand.class,
            bartenderCommandFactory.parse("/show-wishlist", chat));
    }

    @Test
    void testParseShowWishlistMushNotHaveArgsCommand() {
        assertInstanceOf(InvalidCommand.class,
            bartenderCommandFactory.parse("/show-wishlist now", chat));
    }

    @Test
    void testParseShareWishListCommand() {
        assertInstanceOf(ShareWishListCommand.class,
            bartenderCommandFactory.parse("/share-wishlist", chat));
    }

    @Test
    void testParseShareWishListMushNotHaveArgsCommand() {
        assertInstanceOf(InvalidCommand.class,
            bartenderCommandFactory.parse("/share-wishlist please", chat));
    }
}
