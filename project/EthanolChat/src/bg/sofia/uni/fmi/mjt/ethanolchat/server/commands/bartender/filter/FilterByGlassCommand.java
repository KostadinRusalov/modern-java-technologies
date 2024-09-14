package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

public class FilterByGlassCommand extends AbstractFilterCommand {
    public FilterByGlassCommand(String query) {
        super(query, AbstractFilterCommand.theCocktailDB::filterByGlass);
    }
}
