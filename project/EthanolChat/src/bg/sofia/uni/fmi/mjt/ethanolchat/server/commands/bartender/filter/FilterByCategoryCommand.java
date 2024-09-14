package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;

public class FilterByCategoryCommand extends AbstractFilterCommand {
    public FilterByCategoryCommand(String query) {
        super(query, AbstractBartenderCommand.theCocktailDB::filterByCategory);
    }
}
