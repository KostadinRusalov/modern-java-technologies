package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public abstract class AccountBase implements Account {
    protected static final int MAX_COURSES = 100;
    protected final String username;
    protected double balance;
    protected final Course[] courses = new Course[MAX_COURSES];
    protected final double[] grades = new double[MAX_COURSES];
    protected int coursesCount;
    public AccountBase(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount added to balance must be positive");
        }
        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if (course == null || resourcesToComplete == null) {
            throw new IllegalArgumentException("Course and resources cannot be null");
        }

        Course accountCourse = courses[findCourse(course)];
        for (var resource : resourcesToComplete) {
            accountCourse.completeResource(resource);
        }
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (grade < 2.0 || grade > 6.0) {
            throw new IllegalArgumentException("Grade must be in between 2.00 and 6.00");
        }

        int idx = findCourse(course);
        if (!courses[idx].isCompleted()) {
            throw new CourseNotCompletedException("Course is not completed");
        }

        grades[idx] = grade;
    }

    @Override
    public Course getLeastCompletedCourse() {
        if (coursesCount == 0) {
            return null;
        }

        Course leastCompleted = courses[0];
        for (int i = 1; i < coursesCount; ++i) {
            if(courses[i].isLessCompleted(leastCompleted)) {
                leastCompleted = courses[i];
            }
        }
        return leastCompleted;
    }
    protected void buyCourse(Course course, double discount) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (coursesCount == MAX_COURSES) {
            throw new MaxCourseCapacityReachedException("Max course capacity reached");
        }

        for (int i = 0; i < coursesCount; ++i) {
            if (courses[i].equals(course)) {
                throw new CourseAlreadyPurchasedException("Course is already purchased");
            }
        }

        double discountedPrice = (1 - discount) * course.getPrice();
        if (balance < discountedPrice) {
            throw new InsufficientBalanceException("Balance is insufficient");
        }

        balance -= discountedPrice;
        courses[coursesCount++] = course;
    }

    private int findCourse(Course course) throws CourseNotPurchasedException {
        for (int i = 0; i < coursesCount; ++i) {
            if (courses[i].equals(course)) {
                return i;
            }
        }
        throw new CourseNotPurchasedException("Course is not purchased");
    }
}
