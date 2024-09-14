package bg.sofia.uni.fmi.mjt.udemy.account;


import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EducationalAccountTest {
    Resource[] resources = new Resource[]{
            new Resource("R", new ResourceDuration(10)),
    };

    Course[] courses = new Course[]{
            new Course("A", "a", 100, resources, Category.BUSINESS),
            new Course("B", "a", 100, resources, Category.BUSINESS),
            new Course("C", "a", 100, resources, Category.BUSINESS),
            new Course("D", "a", 100, resources, Category.BUSINESS),
            new Course("E", "a", 100, resources, Category.BUSINESS),
            new Course("F", "a", 100, resources, Category.BUSINESS),
            new Course("G", "a", 100, resources, Category.BUSINESS),
            new Course("H", "a", 100, resources, Category.BUSINESS),
            new Course("I", "a", 100, resources, Category.BUSINESS),
            new Course("J", "a", 100, resources, Category.BUSINESS),
    };

    @Test
    void testEducationalAccountDiscount() throws InsufficientBalanceException, MaxCourseCapacityReachedException, CourseAlreadyPurchasedException, CourseNotCompletedException, CourseNotPurchasedException, ResourceNotFoundException {
        Account account = new EducationalAccount("rich-bitch", Double.MAX_VALUE);

        for (int i = 0; i < 5; ++i) {
            account.buyCourse(courses[i]);
            account.completeResourcesFromCourse(courses[i], resources);
            account.completeCourse(courses[i], 5d);
        }

        account.buyCourse(courses[5]);
    }
}
