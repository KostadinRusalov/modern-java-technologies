package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.ArrayList;
import java.util.SequencedCollection;

public record Workout(SequencedCollection<Exercise> exercises) {
    public static Workout empty() {
        return new Workout(new ArrayList<>());
    }
}