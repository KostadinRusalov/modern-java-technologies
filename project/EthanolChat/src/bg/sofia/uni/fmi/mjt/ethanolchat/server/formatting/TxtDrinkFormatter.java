package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;

import java.io.PrintWriter;
import java.io.Writer;

public class TxtDrinkFormatter implements DrinkFormatter {
    @Override
    public String formatExtension() {
        return ".txt";
    }

    @Override
    public void formatDrink(Drink drink, Writer writer) {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.print("Drink: ");
        printWriter.println(drink.name());

        printIfNotNull(drink.category(), "Category: ", printWriter);
        printIfNotNull(drink.type(), "Type: ", printWriter);
        printIfNotNull(drink.glass(), "Served in: ", printWriter);

        printWriter.println("Ingredients:");
        for (int i = 0; i < drink.ingredients().length; i++) {
            if (drink.measurements()[i] != null) {
                printWriter.print(drink.measurements()[i]);
                printWriter.print(" ");
            }
            printWriter.print(drink.ingredients()[i]);
        }
        printWriter.println();
        printWriter.println("How to make:");
        printIfNotNull(drink.instructions(), "", printWriter);
        printWriter.println();
    }

    private void printIfNotNull(String field, String description, PrintWriter printWriter) {
        if (field != null) {
            printWriter.print(description);
            printWriter.print(field);
            printWriter.println();
        }
    }
}
