package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache.Cache;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache.InMemoryCache;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandUtils;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.DrinkFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile.GoFile;

import java.nio.file.Files;
import java.nio.file.Path;

public class ShareWishListCommand extends AbstractBartenderWishListCommand {
    private GoFile goFileAPI;
    private DrinkFormatter drinkFormatter;
    private static Cache<WishList, String> cache = new InMemoryCache<>();

    public ShareWishListCommand(Chat chat, GoFile goFileAPI, DrinkFormatter drinkFormatter) {
        super(null, chat);
        this.goFileAPI = goFileAPI;
        this.drinkFormatter = drinkFormatter;
    }

    @Override
    public String execute() {
        try {
            WishList wishList = wishlists.get(chat.getChatId());
            if (wishList == null || wishList.getDrinks().isEmpty()) {
                return BartenderCommandUtils.NO_WISHLIST;
            }
            if (cache.has(wishList)) {
                return cache.get(wishList);
            }

            GoFile.Response server = goFileAPI.getServer();

            Path wishlistFile = Files.createTempFile(chat.getCode(), drinkFormatter.formatExtension());

            try (var writer = Files.newBufferedWriter(wishlistFile)) {
                wishList.saveWishlist(writer, drinkFormatter);
            }

            GoFile.Response uploadedFile = goFileAPI.uploadFile(server.data().server(), wishlistFile);

            String response = BartenderCommandUtils.formatUploadedFile(uploadedFile.data().downloadPage());

            Files.delete(wishlistFile);
            cache.cache(wishList, response);

            return response;
        } catch (Exception e) {
            return BartenderCommandUtils.CANNOT_SHARE_WISHLIST;
        }
    }
}
