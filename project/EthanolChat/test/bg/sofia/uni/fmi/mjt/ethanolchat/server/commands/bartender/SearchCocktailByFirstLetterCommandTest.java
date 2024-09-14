package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;


import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SearchCocktailByFirstLetterCommandTest extends AbstractBartenderCommandTest {
    String invalidRequest = "Invalid request! Search by one letter only!";

    @Test
    void testSearchCocktailByFirstLetterWithNoLetters() {
        assertEquals(invalidRequest, new SearchCocktailByFirstLetterCommand("").execute());
    }

    @Test
    void testSearchCocktailByFirstLetterWithMayLetters() {
        assertEquals(invalidRequest, new SearchCocktailByFirstLetterCommand("cocktail").execute());
    }

    @Test
    void testSearchCocktailByFirstLetterNoResponse() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByFirstLetter('a')).thenReturn(null);

        assertEquals(Common.unsuccessfulRequest, new SearchCocktailByFirstLetterCommand("a").execute());
    }

    @Test
    void testSearchCocktailByFirstLetterNoDrinks() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByFirstLetter('a'))
            .thenReturn(new TheCocktailDB.Response(null, null));

        assertEquals(Common.unsuccessfulRequest, new SearchCocktailByFirstLetterCommand("a").execute());
    }

    @Test
    void testSearchCocktailByFirstLetterSuccess() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByFirstLetter('w')).thenReturn(Common.whiskeyResponse);

        assertEquals(Common.whiskeyMatch, new SearchCocktailByFirstLetterCommand("w").execute());
    }

    @Test
    void testSearchCocktailByFirstLetterNoResponseBecauseItThrows() {
        when(AbstractBartenderCommand.theCocktailDB.searchDrinkByFirstLetter('a'))
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequestException, new SearchCocktailByFirstLetterCommand("a").execute());
    }
}
