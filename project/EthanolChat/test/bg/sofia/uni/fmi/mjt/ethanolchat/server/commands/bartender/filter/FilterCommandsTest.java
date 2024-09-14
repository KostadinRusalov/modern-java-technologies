package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommandTest;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common.vodkaResponse;
import static bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common.whiskeyResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FilterCommandsTest extends AbstractBartenderCommandTest  {
    static String invalidFilter = "Oops, incorrect filter!";
    @Test
    void testSearchCocktailByNameCommandWithExistingDrink() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("whiskey")).thenReturn(whiskeyResponse);

        assertEquals(Common.whiskeyMatch, new SearchCocktailByNameCommand("whiskey").execute());
    }

    @Test
    void testSearchCocktailByNameWithTwoDrinks() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByName("vodka")).thenReturn(vodkaResponse);

        assertEquals(Common.vodkaMatch, new SearchCocktailByNameCommand("vodka").execute());
    }

    @Test
    void testFilterByAlcoholicCommandCorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByAlcoholic("alcoholic")).thenReturn(vodkaResponse);

        assertEquals(Common.vodkaMatch, new FilterByAlcoholicCommand("alcoholic").execute());
    }

    @Test
    void testFilterByAlcoholicCommandIncorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByAlcoholic("sweet")).thenReturn(null);

        assertEquals(invalidFilter, new FilterByAlcoholicCommand("sweet").execute());
    }

    @Test
    void testFilterByCategoryCommandCorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByCategory("hard")).thenReturn(whiskeyResponse);

        assertEquals(Common.whiskeyMatch, new FilterByCategoryCommand("hard").execute());
    }

    @Test
    void testFilterByCategoryCommandIncorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByCategory("stainless")).thenReturn(null);

        assertEquals(invalidFilter, new FilterByCategoryCommand("stainless").execute());
    }

    @Test
    void testFilterByGlassCommandCorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByGlass("bottle")).thenReturn(vodkaResponse);

        assertEquals(Common.vodkaMatch, new FilterByGlassCommand("bottle").execute());
    }

    @Test
    void testFilterByGlassCommandIncorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByGlass("bowl")).thenReturn(null);

        assertEquals(invalidFilter, new FilterByGlassCommand("bowl").execute());
    }

    @Test
    void testFilterByIngredientCommandCorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByIngredient("ice")).thenReturn(vodkaResponse);

        assertEquals(Common.vodkaMatch, new FilterByIngredientCommand("ice").execute());
    }

    @Test
    void testFilterByIngredientCommandIncorrectFilter() {
        when(AbstractBartenderCommand.theCocktailDB.filterByIngredient("soap")).thenReturn(null);

        assertEquals(invalidFilter, new FilterByIngredientCommand("soap").execute());
    }

    @Test
    void testFilterByIngredientUnsuccessfulRequest() {
        when(AbstractBartenderCommand.theCocktailDB.filterByIngredient(""))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequest, new FilterByIngredientCommand("").execute());
    }
}
