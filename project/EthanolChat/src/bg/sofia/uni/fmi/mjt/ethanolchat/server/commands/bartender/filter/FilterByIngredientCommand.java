package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;

public class FilterByIngredientCommand extends AbstractFilterCommand {
    public FilterByIngredientCommand(String query) {
        super(query, AbstractBartenderCommand.theCocktailDB::filterByIngredient);
    }
}
