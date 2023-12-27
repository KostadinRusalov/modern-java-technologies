package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Member implements GymMember {
    private String personalIdNumber;
    private String name;
    private Address address;
    private int age;
    private Gender gender;

    private Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgram = new EnumMap<>(DayOfWeek.class);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Collections.unmodifiableMap(trainingProgram);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Day and workout cannot be null");
        }
        trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be empty");
        }

        Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        for (var entry : trainingProgram.entrySet()) {
            if (entry.getValue().exercises().getLast().name().equals(exerciseName)) {
                days.add(entry.getKey());
            }
        }

        return days;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException("Day and exercise cannot be null");
        }

        if (trainingProgram.get(day) == null) {
            throw new DayOffException("Day is day off");
        }

        trainingProgram.putIfAbsent(day, Workout.empty());
        trainingProgram.get(day).exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Day cannot be null and exercises cannot be null ot empty");
        }

        if (trainingProgram.get(day) == null) {
            throw new DayOffException("Day is day off");
        }

        trainingProgram.putIfAbsent(day, Workout.empty());
        trainingProgram.get(day).exercises().addAll(exercises);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(personalIdNumber, member.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalIdNumber);
    }
}
