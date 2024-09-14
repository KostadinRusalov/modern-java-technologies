package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        return Math.hypot(other.longitude - longitude, other.latitude - latitude);
    }
}