package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BotCommand;

public abstract class AbstractBartenderCommand implements BotCommand {
    public static TheCocktailDB theCocktailDB;
    private static final String NAME = "bartender";

    public static void configureTheCocktailDB(TheCocktailDB theCocktailDB) {
        AbstractBartenderCommand.theCocktailDB = theCocktailDB;
    }

    @Override
    public String botName() {
        return NAME;
    }
}
