package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;

public class Common {
    public static Drink whiskey = new Drink("1", "whiskey", "hard", "alcoholic", "whiskey glass",
        "Drink straight from the bottle", new String[] {"whiskey"}, new String[] {"whole bottle"});

    public static Drink vodka = new Drink("2", "vodka", "russian", "alcoholic", "whole bottle",
        "Drink straight from the bottle", new String[] {"vodka", "ice"}, new String[] {"whole bottle", "no"});
    public static Drink longVodka = new Drink("3", "long vodka", "russian", "alcoholic", "whole bottle",
        "Drink straight from the bottle", new String[] {"vodka", "ice"}, new String[] {"whole bottle", "no"});

    public static TheCocktailDB.Response whiskeyResponse =
        new TheCocktailDB.Response(new Drink[] {Common.whiskey}, null);
    public static TheCocktailDB.Response vodkaResponse =
        new TheCocktailDB.Response(new Drink[] {Common.vodka, Common.longVodka}, null);

    public static TheCocktailDB.Response nullResponse = new TheCocktailDB.Response(null, null);
    public static String whiskeyMatch = "Hey, there's 1 drink that matches your request: whiskey.";
    public static String vodkaMatch = "Hey, there are 2 that match your request: vodka, long vodka.";
    public static String unsuccessfulRequest = "Sorry, no items match your request.";
    public static String unsuccessfulRequestException = "Sorry, the request was unsuccessful.";

    public static String whiskeyInfo = """
        Hey, here's the info about whiskey
        category: hard
        type: alcoholic
        served in: whiskey glass
        ingredients:
        - whole bottle whiskey
        how to make: Drink straight from the bottle""";
}
