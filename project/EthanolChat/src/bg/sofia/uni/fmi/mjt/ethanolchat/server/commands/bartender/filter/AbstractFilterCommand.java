package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandUtils;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

import java.util.function.Function;

public abstract class AbstractFilterCommand extends AbstractBartenderCommand {
    protected String query;
    protected Function<String, TheCocktailDB.Response> filter;

    protected AbstractFilterCommand(String query, Function<String, TheCocktailDB.Response> filter) {
        this.query = query;
        this.filter = filter;
    }

    @Override
    public String execute() {
        try {
            TheCocktailDB.Response response = filter.apply(query);
            if (response == null) {
                return BartenderCommandUtils.INVALID_FILTER;
            }
            return BartenderCommandUtils.formatDrinks(response.drinks());
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST;
        }
    }
}
