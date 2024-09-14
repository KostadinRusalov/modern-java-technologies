package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CocktailInfoCommandTest extends AbstractBartenderCommandTest {
    @Test
    void testCocktailInfoCommandSuccessful() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        assertEquals(Common.whiskeyInfo, new CocktailInfoCommand("whiskey").execute());
    }

    @Test
    void testCocktailInfoCommandNoDrinks() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("soup"))
            .thenReturn(new TheCocktailDB.Response(null, null));

        assertEquals(Common.unsuccessfulRequest, new CocktailInfoCommand("soup").execute());
    }

    @Test
    void testCocktailInfoCommandUnsuccessful() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey"))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequest, new CocktailInfoCommand("whiskey").execute());
    }

    @Test
    void testExecuteWithDrinks() {
        String response = """
            Hey, here's the info about whiskey
            category: hard
            type: alcoholic
            served in: whiskey glass
            ingredients:
            - whole bottle whiskey
            how to make: Drink straight from the bottle""";

        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(Common.whiskeyResponse);

        assertEquals(response, new CocktailInfoCommand("whiskey").execute());
    }
}
