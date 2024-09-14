package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.BartenderCommandType;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.CocktailInfoCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.RandomCocktailCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.SearchCocktailByFirstLetterCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.SearchIngredientCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.SearchCocktailByNameCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.AddToWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.RemoveFromWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.DrinkFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile.GoFile;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByAlcoholicCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByCategoryCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByGlassCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.filter.FilterByIngredientCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.ShareWishListCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.wishlist.ShowWishListCommand;

import java.util.ArrayList;
import java.util.List;

public class BartenderCommandFactory implements BotCommandFactory {
    private String commandPrefix;
    private static DrinkFormatter drinkFormatter;
    private static GoFile goFile;

    public BartenderCommandFactory(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public static void configureDrinkFormatter(DrinkFormatter drinkFormatter) {
        BartenderCommandFactory.drinkFormatter = drinkFormatter;
    }

    public static void configureGoFile(GoFile goFile) {
        BartenderCommandFactory.goFile = goFile;
    }

    @Override
    public String getCommandPrefix() {
        return commandPrefix;
    }

    @Override
    public String changeCommandPrefix(String prefix) {
        String oldPrefix = commandPrefix;
        commandPrefix = prefix;
        return oldPrefix;
    }

    @Override
    public BotCommand parse(String input, Chat chat) {
        String command = input.substring(commandPrefix.length());
        BotCommand botCommand = parseChatLess(command);
        if (botCommand != null) {
            return botCommand;
        }

        botCommand = parseChatFul(command, chat);
        if (botCommand != null) {
            return botCommand;
        }

        return new InvalidCommand();
    }

    @Override
    public List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        for (var command : BartenderCommandType.values()) {
            commands.add(commandPrefix + command.getCommand() + " " + command.getDescription());
        }
        return commands;
    }

    private static String parseArg(String input, BartenderCommandType type) {
        return input.substring(type.getLength()).strip().toLowerCase();
    }

    private static BotCommand parseChatLess(String command) {
        if (command.startsWith(BartenderCommandType.SEARCH_BY_LETTER.getCommand())) {
            return new SearchCocktailByFirstLetterCommand(parseArg(command, BartenderCommandType.SEARCH_BY_LETTER));
        }
        if (command.startsWith(BartenderCommandType.SEARCH_INGREDIENT.getCommand())) {
            return new SearchIngredientCommand(parseArg(command, BartenderCommandType.SEARCH_INGREDIENT));
        }
        if (command.startsWith(BartenderCommandType.SEARCH.getCommand())) {
            return new SearchCocktailByNameCommand(parseArg(command, BartenderCommandType.SEARCH));
        }
        if (command.startsWith(BartenderCommandType.COCKTAIL_INFO.getCommand())) {
            return new CocktailInfoCommand(parseArg(command, BartenderCommandType.COCKTAIL_INFO));
        }
        if (command.equals(BartenderCommandType.RANDOM.getCommand())) {
            return new RandomCocktailCommand();
        }
        if (command.startsWith(BartenderCommandType.FILTER_BY_INGREDIENT.getCommand())) {
            return new FilterByIngredientCommand(parseArg(command, BartenderCommandType.FILTER_BY_INGREDIENT));
        }
        if (command.startsWith(BartenderCommandType.FILTER_BY_ALCOHOLIC.getCommand())) {
            return new FilterByAlcoholicCommand(parseArg(command, BartenderCommandType.FILTER_BY_ALCOHOLIC));
        }
        if (command.startsWith(BartenderCommandType.FILTER_BY_CATEGORY.getCommand())) {
            return new FilterByCategoryCommand(parseArg(command, BartenderCommandType.FILTER_BY_CATEGORY));
        }
        if (command.startsWith(BartenderCommandType.FILTER_BY_GLASS.getCommand())) {
            return new FilterByGlassCommand(parseArg(command, BartenderCommandType.FILTER_BY_GLASS));
        }
        return null;
    }

    private static BotCommand parseChatFul(String command, Chat chat) {
        if (command.startsWith(BartenderCommandType.ADD_TO_WISHLIST.getCommand())) {
            return new AddToWishListCommand(parseArg(command, BartenderCommandType.ADD_TO_WISHLIST), chat);
        }
        if (command.startsWith(BartenderCommandType.REMOVE_FROM_WISHLIST.getCommand())) {
            return new RemoveFromWishListCommand(parseArg(command, BartenderCommandType.REMOVE_FROM_WISHLIST), chat);
        }
        if (command.equals(BartenderCommandType.SHOW_WISHLIST.getCommand())) {
            return new ShowWishListCommand(chat);
        }
        if (command.equals(BartenderCommandType.SHARE_WISHLIST.getCommand())) {
            return new ShareWishListCommand(chat, goFile, drinkFormatter);
        }
        return null;
    }
}
