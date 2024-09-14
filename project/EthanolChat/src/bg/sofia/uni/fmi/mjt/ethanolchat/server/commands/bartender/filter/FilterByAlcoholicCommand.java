package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

public class FilterByAlcoholicCommand extends AbstractFilterCommand {
    public FilterByAlcoholicCommand(String query) {
        super(query, AbstractFilterCommand.theCocktailDB::filterByAlcoholic);
    }
}
