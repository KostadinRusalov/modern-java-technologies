package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus, Optional<Double> cost, MissionStatus missionStatus) {

    private static final String DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final int ID = 0;
    private static final int COMPANY = 1;
    private static final int LOCATION = 2;
    private static final int DATE = 3;
    private static final int DETAIL = 4;
    private static final int ROCKET_STATUS = 5;
    private static final int COST = 6;
    private static final int MISSION_STATUS = 7;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("\"EEE MMM dd, yyyy\"");

    public static Mission from(String line) {
        String[] tokens = line.split(DELIMITER);

        Optional<Double> cost = tokens[COST].isBlank() ? Optional.empty() :
            Optional.of(Double.valueOf(tokens[COST].substring(1, tokens[COST].length() - 1)));

        return new Mission(
            tokens[ID],
            tokens[COMPANY],
            tokens[LOCATION].substring(1, tokens[LOCATION].length() - 1),
            LocalDate.parse(tokens[DATE], FORMATTER),
            Detail.from(tokens[DETAIL]),
            RocketStatus.from(tokens[ROCKET_STATUS]),
            cost,
            MissionStatus.from(tokens[MISSION_STATUS])
        );
    }

    public static List<Mission> readCSV(Reader csvReader) {
        try (var buffReader = new BufferedReader(csvReader)) {
            return buffReader.lines().skip(1).map(Mission::from).toList();
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception occurred while reading the missions", ex);
        }
    }

    public String getCountry() {
        return location.substring(location.lastIndexOf(' ') + 1);
    }

    public boolean isBetween(LocalDate from, LocalDate to) {
        return date.isAfter(from) && date.isBefore(to) || date.isEqual(from) || date.isEqual(to);
    }
}
