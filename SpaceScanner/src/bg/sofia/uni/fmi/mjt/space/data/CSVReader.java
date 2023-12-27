package bg.sofia.uni.fmi.mjt.space.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class CSVReader {
    private static final int HEADER = 1;
    public static final String DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    public static <T> Collection<T> read(Reader reader, Function<String, T> parse) {
        if (reader == null) {
            return new ArrayList<>();
        }
        try (var buffReader = new BufferedReader(reader)) {
            return buffReader.lines().skip(HEADER).map(parse).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("An exception occurred while reading the data", e);
        }
    }
}
