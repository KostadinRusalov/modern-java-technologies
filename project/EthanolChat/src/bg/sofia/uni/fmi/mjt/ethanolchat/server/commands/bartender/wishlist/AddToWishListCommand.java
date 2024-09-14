package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandUtils;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class AddToWishListCommand extends AbstractBartenderWishListCommand {
    public AddToWishListCommand(String query, Chat chat) {
        super(query, chat);
    }

    @Override
    public String execute() {
        try {
            if (query == null || query.isBlank()) {
                return BartenderCommandUtils.CANNOT_ADD_TO_WISHLIST;
            }

            TheCocktailDB.Response response = theCocktailDB.searchDrinkByName(query);
            if (response == null || response.drinks() == null) {
                return BartenderCommandUtils.CANNOT_ADD_TO_WISHLIST;
            }

            wishlists.putIfAbsent(chat.getChatId(), new WishList());

            if (!wishlists.get(chat.getChatId()).add(response.drinks()[0])) {
                return BartenderCommandUtils.ALREADY_IN_WISHLIST;
            }

            return BartenderCommandUtils.ADDED_TO_WISHLIST;
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST_EXCEPTION;
        }
    }
}
