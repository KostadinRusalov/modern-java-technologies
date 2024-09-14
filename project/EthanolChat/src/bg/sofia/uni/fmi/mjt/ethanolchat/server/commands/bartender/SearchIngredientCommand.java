package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class SearchIngredientCommand extends AbstractBartenderCommand {
    private final String query;

    public SearchIngredientCommand(String query) {
        this.query = query;
    }

    @Override
    public String execute() {
        try {
            TheCocktailDB.Response response = theCocktailDB.searchIngredientByName(query);
            return BartenderCommandUtils.formatIngredientsInfo(response.ingredients());
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST_EXCEPTION;
        }
    }
}
