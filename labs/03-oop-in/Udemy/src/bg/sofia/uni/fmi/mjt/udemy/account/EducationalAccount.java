package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase {
    private static final int MIN_COMPLETED_COURSES = 5;
    private static final double MIN_GRADE_FOR_DISCOUNT = 4.50;
    private int lastDiscounted;

    public EducationalAccount(String username, double balance) {
        super(username, balance);
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        double discount = isEligibleForDiscount() ? AccountType.EDUCATION.getDiscount() : 0;
        super.buyCourse(course, discount);
    }

    private boolean isEligibleForDiscount() {
        if (coursesCount < MIN_COMPLETED_COURSES
                || coursesCount - lastDiscounted < MIN_COMPLETED_COURSES) {
            return false;
        }

        double grade = 0;
        for (int i = 1; i <= MIN_COMPLETED_COURSES; ++i) {
            if (!courses[coursesCount - i].isCompleted()) {
                return false;
            }
            grade += grades[coursesCount - i];
        }

        lastDiscounted = coursesCount;
        return (grade / MIN_COMPLETED_COURSES) >= MIN_GRADE_FOR_DISCOUNT;
    }

}
