package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbstractBartenderCommandTest {
    @Mock
    static TheCocktailDB theCocktailDB;

    @BeforeEach
    void setUp() {
        AbstractBartenderCommand.configureTheCocktailDB(theCocktailDB);
    }
}
