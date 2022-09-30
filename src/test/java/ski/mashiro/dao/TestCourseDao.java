package ski.mashiro.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.mashiro.pojo.Course;

@SpringBootTest
public class TestCourseDao {

    @Autowired
    private CourseDao courseDao;

    @Test
    void testSaveCourse() {
        Course course = new Course();
        course.setCourseName("测试");
        course.setCourseLocation("提篮桥");
        course.setCourseLecturer("陈良宇");
        course.setCourseDate("Sunday");
        course.setCourseTime("10:00-11:30");
        System.out.println((courseDao.saveCourse(course) != 0));
    }

    @Test
    void testDeleteByCourseName() {
        System.out.println(courseDao.deleteByCourseName("测试") != 0);
    }

    @Test
    void testUpdateCourse() {
        Course course = courseDao.getCourseByCourseName("测试");
        course.setCourseLocation("秦城监狱");
        courseDao.updateCourse(course);
    }

    @Test
    void testGetCourseByCourseName() {
        Course course = courseDao.getCourseByCourseName("测试");
        System.out.println(course);
    }

    @Test
    void testListAllByCourseDateCourses() {
        System.out.println(courseDao.listAllByCourseDateCourses("Sunday"));
    }

    @Test
    void testListAllCourses() {
        System.out.println(courseDao.listAllCourses());
    }
}
