package bg.sofia.uni.fmi.mjt.ethanolchat.bots.gofile;

import java.nio.file.Path;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;

public interface GoFile {

    /**
     * Gets the server for file upload.
     * Should be invoked before uploadFile.
     *
     * @return Response containing the server to be used.
     * @throws UnsuccessfulRequestException if the request cannot be completed
     */
    Response getServer();

    /**
     * Uploads file to the server from getServer()
     *
     * @param server the server for uploading files
     * @param file   the file for uploading
     * @return Response with the download page link
     * @throws UnsuccessfulRequestException if the request cannot be completed
     */
    Response uploadFile(String server, Path file);

    record Response(String status, Data data) {
    }

    record Data(String server, String downloadPage) {

    }
}
