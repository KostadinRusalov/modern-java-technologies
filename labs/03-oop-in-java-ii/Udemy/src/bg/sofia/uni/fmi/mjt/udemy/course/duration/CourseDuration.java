package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {
    public static CourseDuration of(Resource[] resources) {
        int totalMinutes = 0;
        for (var resource : resources) {
            totalMinutes += resource.getDuration().minutes();
        }

        return new CourseDuration(totalMinutes / 60, totalMinutes % 60);
    }

    public CourseDuration {
        if (hours < 0 || hours > 24) {
            throw new IllegalArgumentException("Hours must be between 0 and 24");
        }
        if (minutes < 0 || minutes > 60) {
            throw new IllegalArgumentException("Minutes must be between 0 and 60");
        }
    }

    public boolean isLonger(CourseDuration courseDuration) {
        return getTotalMinutes() >= courseDuration.getTotalMinutes();
    }
    private int getTotalMinutes() {
        return hours * 60 + minutes;
    }
}
