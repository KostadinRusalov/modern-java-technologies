package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static Mission of(String line) {
        String[] tokens = line.split(DELIMITER);

        return new Mission(
            tokens[ID],
            tokens[COMPANY],
            tokens[LOCATION],
            LocalDate.parse(tokens[DATE], FORMATTER),
            Detail.of(tokens[DETAIL]),
            RocketStatus.from(tokens[ROCKET_STATUS]),
            getOptionalCostFrom(tokens[COST]),
            MissionStatus.from(tokens[MISSION_STATUS])
        );
    }

    public String getCountry() {
        String[] tokens = location.split(", ");
        return tokens[tokens.length - 1];
    }

    private static Optional<Double> getOptionalCostFrom(String cost) {
        return cost.isBlank() ? Optional.empty() : Optional.of(Double.valueOf(cost));
    }
}
