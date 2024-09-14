package bg.sofia.uni.fmi.mjt.football;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerLine {
    public static final String DELIMITER = ";";
    public static final String POSITIONS_DELIMITER = ",";
    public static final String DATE_DELIMITER = "/";
    public static final int DATE_LENGTH = 3;
    public static final int DATE_MAX_DAY = 31;
    public static final int DATE_MAX_MONTH = 12;
    public static final int DATE_MAX_YEAR = 2023;
    public static final int LENGTH = 13;
    public static final int NAME = 0;
    public static final int FULL_NAME = 1;
    public static final int BIRTH_DATE = 2;
    public static final int AGE = 3;
    public static final int HEIGHT = 4;
    public static final int WEIGHT = 5;
    public static final int POSITIONS = 6;
    public static final int NATIONALITY = 7;
    public static final int OVERALL_RATING = 8;
    public static final int POTENTIAL = 9;
    public static final int VALUE_EURO = 10;
    public static final int WAGE_EURO = 11;
    public static final int PREFERRED_FOOT = 12;

    private static final Set<String> POSITIONS_SET;

    static {
        POSITIONS_SET = new HashSet<>(Arrays.stream(Position.values())
            .map(Position::name)
            .toList()
        );
    }

    public static boolean isValid(String line) {
        String[] tokens = line.split(DELIMITER);
        boolean b = tokens.length == LENGTH &&
            isValidDate(tokens[BIRTH_DATE]) &&
            isValidInt(tokens[AGE]) &&
            isValidDouble(tokens[HEIGHT]) &&
            isValidDouble(tokens[WEIGHT]) &&
            areValidPositions(tokens[POSITIONS]) &&
            isValidInt(tokens[OVERALL_RATING]) &&
            isValidInt(tokens[POTENTIAL]) &&
            isValidLong(tokens[VALUE_EURO]) &&
            isValidLong(tokens[WAGE_EURO]) &&
            isValidFoot(tokens[PREFERRED_FOOT]);

        return b;
    }

    private static boolean isValidDate(String token) {
        String[] values = token.split(DATE_DELIMITER);
        if (values.length != DATE_LENGTH ||
            !isValidInt(values[0]) ||
            !isValidInt(values[1]) ||
            !isValidInt(values[2])) {
            return false;
        }
        int month = Integer.parseInt(values[0]);
        int day = Integer.parseInt(values[1]);
        int year = Integer.parseInt(values[2]);

        return month > 0 && month <= DATE_MAX_MONTH &&
            day > 0 && day <= DATE_MAX_DAY &&
            year > 0 && year < DATE_MAX_YEAR;
    }

    private static boolean isValidInt(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isValidLong(String token) {
        try {
            Long.parseLong(token);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isValidDouble(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean areValidPositions(String token) {
        return Arrays.stream(token.split(POSITIONS_DELIMITER)).allMatch(POSITIONS_SET::contains);
    }

    private static boolean isValidFoot(String token) {
        return token.equalsIgnoreCase("LEFT") || token.equalsIgnoreCase("RIGHT");
    }
}
