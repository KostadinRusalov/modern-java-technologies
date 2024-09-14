package bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.DefaultLogger;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoFileClient implements GoFile {
    private static final HttpRequest GET_SERVER_REQUEST = HttpRequest
        .newBuilder(URI.create("https://api.gofile.io/getServer"))
        .build();

    private HttpClient client;
    private Gson gson;
    private Logger logger;

    public GoFileClient() {
        this(HttpClient.newHttpClient(), new DefaultLogger(), new Gson());
    }

    public GoFileClient(HttpClient client, Logger logger, Gson gson) {
        this.client = client;
        this.logger = logger;
        this.gson = gson;
    }

    @Override
    public Response getServer() {
        try {
            HttpResponse<String> httpResponse = client.send(GET_SERVER_REQUEST, HttpResponse.BodyHandlers.ofString());
            logger.log("Sending a request to " + httpResponse.uri());

            if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new UnsuccessfulRequestException(
                    "Unsuccessful request to GoFile server with code " + httpResponse.statusCode()
                );
            }

            return gson.fromJson(httpResponse.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw new UnsuccessfulRequestException("Communicating with GoFile server was unsuccessful", e);
        }
    }

    @Override
    public Response uploadFile(String server, Path file) {
        try {
            String boundary = UUID.randomUUID().toString();

            HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create(STR. "https://\{ server }.gofile.io/uploadFile" ))
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMultipartFile(file, boundary))
                .build();

            logger.log("Uploading a file a request to " + httpRequest.uri());

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
                logger.log("Unsuccessful attempt to upload file to GoFile: " + httpResponse.body());
                throw new UnsuccessfulRequestException("Unsuccessful attempt to upload file to GoFile");
            }

            return gson.fromJson(httpResponse.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw new UnsuccessfulRequestException("Communicating with GoFile server was unsuccessful", e);
        }
    }

    /**
     * Took it from
     * <a href="https://stackoverflow.com/questions/46392160/java-9-httpclient-send-a-multipart-form-data-request">
     *     stackoverflow
     * </a>
     * and made it work for the specific api - not the best approach, but it works
     *
     * @param path     the file for uploading
     * @param boundary some mime magic
     * @return body publisher
     * @throws IOException
     */
    private static HttpRequest.BodyPublisher ofMultipartFile(Path path, String boundary) throws IOException {
        List<byte[]> bytes = new ArrayList<>();

        String filename = path.getFileName().toString();
        String contentType = Files.probeContentType(path);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        String header = "--" + boundary + "\r\n"
            + "Content-Disposition: form-data; name=file; filename=" + filename + "\r\n" +
            "Content-Type: " + contentType + "\r\n\r\n";
        bytes.add(header.getBytes(StandardCharsets.UTF_8));

        try (var fileContent = Files.newInputStream(path)) {
            bytes.add(fileContent.readAllBytes());
        }

        bytes.add("\r\n".getBytes(StandardCharsets.UTF_8));
        bytes.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(bytes);
    }
}
