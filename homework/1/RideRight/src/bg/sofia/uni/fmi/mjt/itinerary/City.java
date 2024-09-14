package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.norm.ManhattanDistance;

import java.math.BigDecimal;

public record City(String name, Location location)
    implements ManhattanDistance<City, BigDecimal>, Comparable<City> {
    @Override
    public BigDecimal distanceTo(City other) {
        return location.distanceTo(other.location);
    }

    @Override
    public int compareTo(City other) {
        return name.compareTo(other.name);
    }
}