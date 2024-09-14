package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class SearchCocktailByFirstLetterCommand extends AbstractBartenderCommand {
    private final String query;

    public SearchCocktailByFirstLetterCommand(String query) {
        this.query = query;
    }

    @Override
    public String execute() {
        try {
            if (query == null || query.length() != 1) {
                return "Invalid request! Search by one letter only!";
            }
            TheCocktailDB.Response response = theCocktailDB.searchDrinkByFirstLetter(query.charAt(0));
            if (response == null) {
                return BartenderCommandUtils.UNSUCCESSFUL_REQUEST;
            }
            return BartenderCommandUtils.formatDrinks(response.drinks());
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST_EXCEPTION;
        }
    }
}
