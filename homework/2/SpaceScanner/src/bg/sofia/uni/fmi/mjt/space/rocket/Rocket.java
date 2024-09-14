package bg.sofia.uni.fmi.mjt.space.rocket;

import bg.sofia.uni.fmi.mjt.space.data.CSVReader;

import java.util.Optional;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int WIKI = 2;
    private static final int HEIGHT = 3;

    public static Rocket from(String line) {
        String[] tokens = line.split(CSVReader.DELIMITER);

        Optional<String> wiki = tokens.length <= WIKI || tokens[WIKI].isBlank() ?
            Optional.empty() : Optional.of(tokens[WIKI]);

        Optional<Double> height = tokens.length <= HEIGHT ? Optional.empty() :
            Optional.of(Double.valueOf(tokens[HEIGHT].substring(0, tokens[HEIGHT].length() - 2)));

        if (tokens[NAME].startsWith("\"")) {
            tokens[NAME] = tokens[NAME].substring(1, tokens[NAME].length() - 1);
        }

        return new Rocket(tokens[ID], tokens[NAME], wiki, height);
    }
}