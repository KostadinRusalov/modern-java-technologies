package bg.sofia.uni.fmi.mjt.space.rocket;

public enum RocketStatus {
    STATUS_RETIRED("StatusRetired"),
    STATUS_ACTIVE("StatusActive");

    private final String value;

    RocketStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static RocketStatus from(String rocketStatus) {
        return switch (rocketStatus) {
            case "StatusActive" -> RocketStatus.STATUS_ACTIVE;
            case "StatusRetired" -> RocketStatus.STATUS_RETIRED;
            default -> throw new IllegalArgumentException("Invalid rocket status");
        };
    }
}