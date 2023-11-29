package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedEdge;
import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price)
    implements WeightedEdge<City, BigDecimal> {

    @Override
    public BigDecimal weight() {
        return price.add(price.multiply(vehicleType.getGreenTax()));
    }
}