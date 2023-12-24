package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {
    private static final String DELIMITER = " \\| ";
    private static final int ROCKET_NAME = 0;
    private static final int PAYLOAD = 1;

    public static Detail of(String detail) {
        String[] tokens = detail.split(DELIMITER);
        return new Detail(tokens[ROCKET_NAME], tokens[PAYLOAD]);
    }
}
