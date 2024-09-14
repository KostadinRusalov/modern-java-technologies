package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.comparators.GymMemberByIdComparator;
import bg.sofia.uni.fmi.mjt.gym.member.comparators.GymMemberByNameComparator;
import bg.sofia.uni.fmi.mjt.gym.member.comparators.GymMemberByProximityComparator;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;

    private SortedSet<GymMember> members;

    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        members = new TreeSet<>(new GymMemberByIdComparator());
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        SortedSet<GymMember> sortedMembers = new TreeSet<>(new GymMemberByNameComparator());
        sortedMembers.addAll(members);
        return Collections.unmodifiableSortedSet(sortedMembers);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> sortedMembers = new TreeSet<>(new GymMemberByProximityComparator(address));
        sortedMembers.addAll(members);
        return Collections.unmodifiableSortedSet(sortedMembers);
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        if (members.size() == capacity) {
            throw new GymCapacityExceededException("Gym capacity is exceeded");
        }

        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Members cannot be null or empty");
        }

        if (this.members.size() + members.size() >= capacity) {
            throw new GymCapacityExceededException("Gym capacity is exceeded");
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (day == null || exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Day cannot be null and exercise name cannot be null or empty");
        }

        for (var member : members) {
            if (member
                .getTrainingProgram()
                .getOrDefault(day, Workout.empty())
                .exercises()
                .contains(Exercise.of(exerciseName))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty");
        }

        Map<DayOfWeek, List<String>> dailyList = new EnumMap<>(DayOfWeek.class);
        for (var member : members) {
            for (var day : DayOfWeek.values()) {
                if (member
                    .getTrainingProgram()
                    .getOrDefault(day, Workout.empty())
                    .exercises()
                    .contains(Exercise.of(exerciseName))) {
                    dailyList.putIfAbsent(day, new ArrayList<>());
                    dailyList.get(day).add(member.getName());
                }
            }
        }

        return dailyList;
    }
}
