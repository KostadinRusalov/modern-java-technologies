package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    private final Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("Utility type and billable cannot be null");
        }

        return taxRates.get(utilityType) * switch (utilityType) {
            case WATER -> billable.getWaterConsumption();
            case ELECTRICITY -> billable.getElectricityConsumption();
            case NATURAL_GAS -> billable.getNaturalGasConsumption();
        };
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("Billable cannot be null");
        }

        return taxRates.get(UtilityType.WATER) * billable.getWaterConsumption() +
            taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption() +
            taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Billables cannot be null");
        }

        Map<UtilityType, Double> costsDifference = new EnumMap<>(UtilityType.class);
        for (var type : UtilityType.values()) {
            costsDifference.put(type, getCostDifference(type, firstBillable, secondBillable));
        }

        return Collections.unmodifiableMap(costsDifference);
    }

    private <T extends Billable> double getCostDifference(UtilityType type, T first, T second) {
        return taxRates.get(type) * switch (type) {
            case WATER -> Math.abs(first.getWaterConsumption() - second.getWaterConsumption());
            case ELECTRICITY -> Math.abs(first.getElectricityConsumption() - second.getElectricityConsumption());
            case NATURAL_GAS -> Math.abs(first.getNaturalGasConsumption() - second.getNaturalGasConsumption());
        };
    }
}
