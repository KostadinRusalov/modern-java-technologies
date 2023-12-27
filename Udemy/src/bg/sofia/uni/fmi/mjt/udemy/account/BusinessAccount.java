package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase {
    private final Category[] permittedCategories;

    public BusinessAccount(String username, double balance, Category[] categories) {
        super(username, balance);
        this.permittedCategories = categories;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (!isPermitted(course.getCategory())) {
            throw new IllegalArgumentException("Category is not permitted");
        }
        super.buyCourse(course, AccountType.BUSINESS.getDiscount());
    }

    private boolean isPermitted(Category category) {
        for (var permitted : permittedCategories) {
            if (category.equals(permitted)) {
                return true;
            }
        }
        return false;
    }
}
