package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandUtils;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public class RemoveFromWishListCommand extends AbstractBartenderWishListCommand {
    public RemoveFromWishListCommand(String query, Chat chat) {
        super(query, chat);
    }

    @Override
    public String execute() {
        try {
            if (query == null || query.isBlank()) {
                return BartenderCommandUtils.CANNOT_REMOVE_FROM_WISHLIST;
            }

            TheCocktailDB.Response response = theCocktailDB.searchDrinkByName(query);
            if (response == null || response.drinks() == null) {
                return BartenderCommandUtils.CANNOT_REMOVE_FROM_WISHLIST;
            }

            WishList wishList = wishlists.get(chat.getChatId());
            if (wishList == null) {
                return BartenderCommandUtils.NO_WISHLIST;
            }

            if (!wishList.remove(response.drinks()[0])) {
                return BartenderCommandUtils.NO_DRINK;
            }

            return BartenderCommandUtils.REMOVED;
        } catch (IllegalArgumentException | UnsuccessfulRequestException _) {
            return BartenderCommandUtils.UNSUCCESSFUL_REQUEST_EXCEPTION;
        }
    }
}
