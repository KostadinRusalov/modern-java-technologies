package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Ingredient;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SearchIngredientCommandTest extends AbstractBartenderCommandTest {

    @Test
    void testSearchIngredientWhenWhereIsNoIngredient() {
        when(AbstractBartenderCommand.theCocktailDB.searchIngredientByName("soap")).thenReturn(
            new TheCocktailDB.Response(null, null));

        assertEquals(Common.unsuccessfulRequest, new SearchIngredientCommand("soap").execute());
    }

    @Test
    void testSearchIngredientWhenWhereIsOneIngredient() {
        Ingredient vodka = new Ingredient("1", "vodka", "no memories after it", "beluga", "40");

        TheCocktailDB.Response vodkaResponse = new TheCocktailDB.Response(null, new Ingredient[] {vodka});

        String response = """
            Hey, here's the info about vodka
            description: no memories after it
            type: beluga
            ABV: 40""";
        when(AbstractBartenderCommand.theCocktailDB.searchIngredientByName("vodka")).thenReturn(vodkaResponse);

        assertEquals(response, new SearchIngredientCommand("vodka").execute());
    }

    @Test
    void testSearchIngredientWhenThereIsNoConnection() {
        when(AbstractBartenderCommand.theCocktailDB.searchIngredientByName("whiskey"))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequestException, new SearchIngredientCommand("whiskey").execute());
    }
}
