package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {
    private static final String DELIMITER = "\\s*\\|\\s*";
    private static final int ROCKET_NAME = 0;
    private static final int PAYLOAD = 1;

    public static Detail from(String detail) {
        String[] tokens = detail.startsWith("\"") ?
            detail.substring(1, detail.length() - 1).split(DELIMITER) : detail.split(DELIMITER);

        return new Detail(tokens[ROCKET_NAME], tokens[PAYLOAD]);
    }
}
