package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBartenderWishListCommand extends AbstractBartenderCommand {
    public static Map<ChatId, WishList> wishlists = new HashMap<>();
    protected String query;
    protected Chat chat;

    protected AbstractBartenderWishListCommand(String query, Chat chat) {
        this.query = query;
        this.chat = chat;
    }

    public static void configureWishlistStorage(Map<ChatId, WishList> wishlists) {
        AbstractBartenderWishListCommand.wishlists = wishlists;
    }
}
