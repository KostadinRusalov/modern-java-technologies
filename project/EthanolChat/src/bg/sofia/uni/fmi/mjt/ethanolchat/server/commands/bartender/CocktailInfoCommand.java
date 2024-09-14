package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class CocktailInfoCommand extends AbstractBartenderCommand {
    protected String query;

    public CocktailInfoCommand(String query) {
        this.query = query;
    }

    @Override
    public String execute() {
        try {
            TheCocktailDB.Response response = theCocktailDB.searchDrinkByName(query);
            return BartenderCommandUtils.formatDrinksInfo(response.drinks());
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST;
        }
    }
}
