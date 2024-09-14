package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandUtils;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;

public class ShowWishListCommand extends AbstractBartenderWishListCommand {
    public ShowWishListCommand(Chat chat) {
        super(null, chat);
    }

    @Override
    public String execute() {
        return BartenderCommandUtils.formatWishListDrinks(wishlists.get(chat.getChatId()));
    }
}
