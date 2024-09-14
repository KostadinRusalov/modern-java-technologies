package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

import java.util.Arrays;

public class Udemy implements LearningPlatform {
    private final Account[] accounts;
    private final Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        for (var course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }

        throw new CourseNotFoundException("There is no course with name " + name);
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank() || !isKeyword(keyword)) {
            throw new IllegalArgumentException("Keyword cannot be null, empty not a keyword");
        }

        Course[] byKeyword = new Course[courses.length];
        int count = 0;
        for (var course : courses) {
            if (course.getName().contains(keyword)
                    || course.getDescription().contains(keyword)) {
                byKeyword[count++] = course;
            }
        }

        return Arrays.copyOf(byKeyword, count);
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        Course[] byCategory = new Course[courses.length];
        int count = 0;
        for (var course : courses) {
            if (course.getCategory().equals(category)) {
                byCategory[count++] = course;
            }
        }

        return Arrays.copyOf(byCategory, count);
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        for (var account : accounts) {
            if (account.getUsername().equals(name)) {
                return account;
            }
        }

        throw new AccountNotFoundException("There is no account with name " + name);
    }

    @Override
    public Course getLongestCourse() {
        if (courses.length == 0) {
            return null;
        }

        Course longest = courses[0];
        for (var course : courses) {
            if (course.getTotalTime().isLonger(longest.getTotalTime())) {
                longest = course;
            }
        }

        return longest;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        Course[] byCategory = getAllCoursesByCategory(category);
        if (byCategory.length == 0) {
            return null;
        }

        Course cheapest = byCategory[0];
        for (var course : byCategory) {
            if (course.getPrice() < cheapest.getPrice()) {
                cheapest = course;
            }
        }

        return cheapest;
    }

    private boolean isKeyword(String keyword) {
        for (char letter : keyword.toCharArray()) {
            if (!Character.isLetter(letter)) {
                return false;
            }
        }
        return true;
    }
}
