package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private final int buildableArea;
    private final Map<String, E> buildables;
    private int remainingArea;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.buildables = new HashMap<>();
        this.remainingArea = buildableArea;
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank() || buildable == null) {
            throw new IllegalArgumentException("Address and buildable cannot be null.");
        }

        if (buildables.containsKey(address)) {
            throw new BuildableAlreadyExistsException("Buildable already exists.");
        }

        if (buildable.getArea() > remainingArea) {
            throw new InsufficientPlotAreaException("Insufficient plot area.");
        }

        buildables.put(address, buildable);
        remainingArea -= buildable.getArea();
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Buildable cannot be null or empty");
        }

        for (var address : buildables.keySet()) {
            if (this.buildables.containsKey(address)) {
                throw new BuildableAlreadyExistsException("Buildable already exists.");
            }
        }

        int area = 0;
        for (E buildable : buildables.values()) {
            area += buildable.getArea();
        }

        if (area > remainingArea) {
            throw new InsufficientPlotAreaException("Insufficient plot area");
        }

        remainingArea -= area;
        this.buildables.putAll(buildables);
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }

        if (!buildables.containsKey(address)) {
            throw new BuildableNotFoundException("Buildable does not exist");
        }

        remainingArea += buildables.remove(address).getArea();
    }

    @Override
    public void demolishAll() {
        remainingArea = buildableArea;
        buildables.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(Collections.unmodifiableMap(buildables));
    }

    @Override
    public int getRemainingBuildableArea() {
        return remainingArea;
    }
}
