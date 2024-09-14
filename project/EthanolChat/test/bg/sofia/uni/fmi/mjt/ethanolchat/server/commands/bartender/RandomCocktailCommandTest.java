package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RandomCocktailCommandTest extends AbstractBartenderCommandTest {
    @Test
    void testCocktailInfoCommandSuccessful() {
        when(AbstractBartenderCommand.theCocktailDB.searchRandomDrink()).thenReturn(Common.whiskeyResponse);

        assertEquals(Common.whiskeyInfo, new RandomCocktailCommand().execute());
    }

    @Test
    void testCocktailInfoCommandUnsuccessful() {
        when(AbstractBartenderCommand.theCocktailDB.searchRandomDrink())
            .thenThrow(UnsuccessfulRequestException.class);

        assertEquals(Common.unsuccessfulRequest, new RandomCocktailCommand().execute());
    }
}
