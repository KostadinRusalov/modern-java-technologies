package bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.Chat;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TxtFileChatFormatterTest {
    TxtFileChatFormatter txtFileChatFormatter = new TxtFileChatFormatter();
    Chat chat;

    String content;

    {
        chat = new Chat("mjt", "100yo", LocalDateTime.parse("2023-10-04T19:14:59"));
        chat.setMessages(List.of(
            "Thread dump - da si izdumpish tredovete",
            "Selectorut e kato barman",
            "Ednorozite na uprajnenieto"
        ));

        content = """
            mjt
            2023-10-04T19:14:59
            100yo
            Thread dump - da si izdumpish tredovete
            Selectorut e kato barman
            Ednorozite na uprajnenieto
            """;
    }


    @Test
    void testFormatName() {
        String expected = "mjt-2023-10-04T19:14";

        assertEquals(expected, txtFileChatFormatter.formatName(chat.getChatId()));
    }

    @Test
    void testFormatContent() {
        StringWriter writer = new StringWriter();
        txtFileChatFormatter.formatContent(chat, writer);

        assertEquals(content, writer.toString());
    }

    @Test
    void testReadContent() {
        Chat chatFromReader = txtFileChatFormatter.readContent(new StringReader(content));

        assertEquals(chat.getCode(), chatFromReader.getCode());
        assertEquals(chat.getCreatedBy(), chatFromReader.getCreatedBy());
        assertEquals(chat.getCreatedOn(), chatFromReader.getCreatedOn());
        assertEquals(chat.getMessages(), chatFromReader.getMessages());
    }
}
