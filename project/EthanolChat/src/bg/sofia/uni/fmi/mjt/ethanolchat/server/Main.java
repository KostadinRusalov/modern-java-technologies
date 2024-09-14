package bg.sofia.uni.fmi.mjt.ethanolchat.server;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDBClient;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.BartenderCommandFactory;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.AbstractBartenderCommand;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.SimpleMessageFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.TxtFileChatFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.FileInactiveChatRepository;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories.InMemoryActiveChatRepository;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile.GoFileClient;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache.InMemoryCache;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache.TheCocktailDBCacheProxy;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.TxtDrinkFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.DefaultLogger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatManager;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.Set;

public class Main {
    private static final int DEFAULT_PORT = 4444;
    public static void main(String[] args) {
        AbstractBartenderCommand.configureTheCocktailDB(
            new TheCocktailDBCacheProxy(
                new TheCocktailDBClient(HttpClient.newHttpClient(), new DefaultLogger()),
                new InMemoryCache<>(), new InMemoryCache<>(), new InMemoryCache<>(),
                new InMemoryCache<>(), new InMemoryCache<>(), new InMemoryCache<>(), new InMemoryCache<>()
            )
        );

        BartenderCommandFactory.configureGoFile(new GoFileClient());
        BartenderCommandFactory.configureDrinkFormatter(new TxtDrinkFormatter());

        Server server = new Server(
            "localhost",
            DEFAULT_PORT,
            new ChatManager(
                new InMemoryActiveChatRepository(),
                new FileInactiveChatRepository(
                    Path.of("file-chat-db"),
                    new TxtFileChatFormatter(),
                    new DefaultLogger()),
                new SimpleMessageFormatter(),
                Set.of(new BartenderCommandFactory("/")),
                new DefaultLogger()
            ),
            new DefaultLogger());

        server.start();
    }
}
