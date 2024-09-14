package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

public enum BartenderCommandType {
    SEARCH("search", "[name] - searches for drinks with that name"),
    COCKTAIL_INFO("info", "[name] - shows info about the drink"),
    RANDOM("random", "- shows info about random drink"),
    SEARCH_BY_LETTER("search-by-letter", "[letter] - searches for drinks with that letter"),
    SEARCH_INGREDIENT("search-ingredient", "[name] - shows info about that ingredient"),
    FILTER_BY_INGREDIENT("filter-by-ingredient", "[ingredient] - filters drinks with that ingredient"),
    FILTER_BY_ALCOHOLIC("filter-by-alcoholic", "[alcoholic/non alcoholic/optional alcohol] - filters by type"),
    FILTER_BY_CATEGORY("filter-by-category", "[category] - filters by category"),
    FILTER_BY_GLASS("filter-by-glass", "[glass] - filters by glass"),
    ADD_TO_WISHLIST("add-to-wishlist", "[drink] - add drink to wishlist"),
    REMOVE_FROM_WISHLIST("remove-from-wishlist", "[drink] - removes from wishlist"),
    SHOW_WISHLIST("show-wishlist", "- shows drinks in wishlist"),
    SHARE_WISHLIST("share-wishlist", "shares a file with the drinks from the wishlist");

    private final String command;
    private final String description;

    BartenderCommandType(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public int getLength() {
        return command.length();
    }
}
