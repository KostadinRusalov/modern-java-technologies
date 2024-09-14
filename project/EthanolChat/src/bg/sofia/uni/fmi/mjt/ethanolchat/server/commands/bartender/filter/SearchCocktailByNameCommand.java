package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;

public class SearchCocktailByNameCommand extends AbstractFilterCommand {
    public SearchCocktailByNameCommand(String query) {
        super(query, AbstractBartenderCommand.theCocktailDB::searchDrinkByName);
    }
}