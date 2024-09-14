package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TxtDrinkFormatterTest {
    @Test
    void testFormatDrink() {
        String expected = """
            Drink: whiskey
            Category: hard
            Type: alcoholic
            Served in: whiskey glass
            Ingredients:
            whole bottle whiskey
            How to make:
            Drink straight from the bottle
                        
            """;

        StringWriter writer = new StringWriter();
        new TxtDrinkFormatter().formatDrink(Common.whiskey, writer);

        assertEquals(expected, writer.toString());
    }
}
