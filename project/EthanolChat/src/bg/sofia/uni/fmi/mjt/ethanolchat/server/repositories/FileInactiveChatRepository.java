package bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.FileChatFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class FileInactiveChatRepository implements InactiveChatRepository {
    private final Path baseDir;
    private FileChatFormatter chatFormatter;
    private Logger logger;

    public FileInactiveChatRepository(Path path, FileChatFormatter chatFormatter, Logger logger) {
        this.baseDir = path;
        this.chatFormatter = chatFormatter;
        this.logger = logger;
    }

    public Path getBaseDir() {
        return baseDir;
    }

    @Override
    public void create(Chat chat) {
        Path chatFile = baseDir.resolve(chatFormatter.formatName(chat.getChatId()));
        try (var fileWriter = Files.newBufferedWriter(chatFile, CREATE)) {
            chatFormatter.formatContent(chat, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            logger.log("Cannot save chat to file due to " + e.getMessage());
        }
    }

    @Override
    public Chat read(ChatId key) {
        Path chatFile = baseDir.resolve(chatFormatter.formatName(key));
        if (Files.notExists(chatFile)) {
            return null;
        }

        return readContent(chatFile);
    }

    @Override
    public Collection<Chat> readAll() {
        try (var dirSteam = Files.newDirectoryStream(baseDir)) {
            List<Chat> chats = new ArrayList<>();
            for (Path chatFile : dirSteam) {
                chats.add(readContent(chatFile));
            }
            return chats;
        } catch (IOException e) {
            logger.log("Cannot read chat from file due to " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Chat chat) {
        Path chatFile = baseDir.resolve(chatFormatter.formatName(new ChatId(chat.getCode(), chat.getCreatedOn())));
        try (var fileWriter = Files.newBufferedWriter(chatFile, TRUNCATE_EXISTING)) {
            chatFormatter.formatContent(chat, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            logger.log("Cannot update chat to file due to " + e.getMessage());
        }
    }

    @Override
    public void delete(ChatId key) {
        try {
            Files.delete(baseDir.resolve(chatFormatter.formatName(key)));
        } catch (IOException e) {
            logger.log("Cannot delete chat to file due to " + e.getMessage());
        }
    }

    private Chat readContent(Path chatFile) {
        try (var fileReader = Files.newBufferedReader(chatFile)) {
            return chatFormatter.readContent(fileReader);
        } catch (IOException e) {
            logger.log("Cannot update chat to file due to " + e.getMessage());
            return null;
        }
    }
}