package bg.sofia.uni.fmi.mjt.gym.member.comparators;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class GymMemberByProximityComparator implements Comparator<GymMember> {
    private final Address reference;

    public GymMemberByProximityComparator(Address reference) {
        this.reference = reference;
    }

    @Override
    public int compare(GymMember o1, GymMember o2) {
        return Double.compare(o1.getAddress().getDistanceTo(reference), o2.getAddress().getDistanceTo(reference));
    }
}