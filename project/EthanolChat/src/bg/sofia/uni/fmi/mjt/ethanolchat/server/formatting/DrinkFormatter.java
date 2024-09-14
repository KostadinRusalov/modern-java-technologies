package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;

import java.io.Writer;

public interface DrinkFormatter {
    String formatExtension();

    void formatDrink(Drink drink, Writer writer);
}
