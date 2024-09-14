package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.ChatId;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TxtFileChatFormatter implements FileChatFormatter {
    @Override
    public String formatName(ChatId chatId) {
        return chatId.code() + "-" + chatId.createdOn().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public void formatContent(Chat chat, Writer writer) {
        PrintWriter fileWriter = new PrintWriter(writer);
        fileWriter.println(chat.getCode());
        fileWriter.println(chat.getCreatedOn());
        fileWriter.println(chat.getCreatedBy());
        chat.getMessages().forEach(fileWriter::println);
    }

    @Override
    public Chat readContent(Reader reader) {
        Scanner fileScanner = new Scanner(reader);

        String code = fileScanner.nextLine();
        String createdOn = fileScanner.nextLine();
        String creator = fileScanner.nextLine();

        List<String> messages = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            String message = fileScanner.nextLine();
            messages.add(message);
        }

        Chat chat = new Chat(code, creator, LocalDateTime.parse(createdOn));
        chat.setMessages(messages);

        return chat;
    }
}
