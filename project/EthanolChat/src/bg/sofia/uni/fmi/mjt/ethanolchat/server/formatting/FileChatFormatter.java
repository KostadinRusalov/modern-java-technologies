package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;

import java.io.Reader;
import java.io.Writer;

public interface FileChatFormatter {
    String formatName(ChatId chatId);

    void formatContent(Chat chat, Writer writer);

    Chat readContent(Reader reader);
}
