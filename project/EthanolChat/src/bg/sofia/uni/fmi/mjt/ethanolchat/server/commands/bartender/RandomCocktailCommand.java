package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class RandomCocktailCommand extends AbstractBartenderCommand {
    @Override
    public String execute() {
        try {
            TheCocktailDB.Response response = theCocktailDB.searchRandomDrink();
            return BartenderCommandUtils.formatDrinksInfo(response.drinks());
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST;
        }
    }
}
