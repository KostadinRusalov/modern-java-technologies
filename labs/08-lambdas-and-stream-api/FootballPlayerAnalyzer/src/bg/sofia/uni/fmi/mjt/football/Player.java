package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    public static Player of(String line) {
        String[] tokens = line.split(PlayerLine.DELIMITER);
        return new Player(
            tokens[PlayerLine.NAME],
            tokens[PlayerLine.FULL_NAME],
            getDate(tokens[PlayerLine.BIRTH_DATE]),
            Integer.parseInt(tokens[PlayerLine.AGE]),
            Double.parseDouble(tokens[PlayerLine.HEIGHT]),
            Double.parseDouble(tokens[PlayerLine.WEIGHT]),
            getPositions(tokens[PlayerLine.POSITIONS]),
            tokens[PlayerLine.NATIONALITY],
            Integer.parseInt(tokens[PlayerLine.OVERALL_RATING]),
            Integer.parseInt(tokens[PlayerLine.POTENTIAL]),
            Long.parseLong(tokens[PlayerLine.VALUE_EURO]),
            Long.parseLong(tokens[PlayerLine.WAGE_EURO]),
            Foot.valueOf(tokens[PlayerLine.PREFERRED_FOOT].toUpperCase())
        );
    }

    public double prospect() {
        return (double) (overallRating + potential) / age;
    }

    public boolean isSimilarTo(Player other) {
        final int maxDiff = 4;
        Set<Position> positions = EnumSet.copyOf(positions());
        positions.retainAll(other.positions);

        return !positions.isEmpty() &&
            preferredFoot == other.preferredFoot &&
            Math.abs(overallRating - other.overallRating) < maxDiff;
    }

    private static LocalDate getDate(String date) {
        return null;
    }

    private static List<Position> getPositions(String positions) {
        return Arrays.stream(positions.split(PlayerLine.POSITIONS_DELIMITER))
            .map(Position::valueOf)
            .toList();
    }
}
