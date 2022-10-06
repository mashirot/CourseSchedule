package ski.mashiro.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.mashiro.pojo.Course;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestCourseDao {

    @Autowired
    private CourseDao courseDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSaveCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("测试");
        course.setCourseLocation("提篮桥");
        course.setCourseLecturer("陈良宇");
        Map<String, String> map = new HashMap<>();
        map.put("Sunday", "10:00-11:30");
        map.put("Monday", "14:00-15:30");
        course.setCourseNormalDate(map);
        course.setCourseDate(objectMapper.writeValueAsString(course.getCourseNormalDate()));
        System.out.println((courseDao.saveCourse(course, "") != 0));
    }

    @Test
    void testDeleteByCourseName() {
        System.out.println(courseDao.deleteByCourseName("测试", "") != 0);
    }

    @Test
    void testUpdateCourse() {
        Course course = courseDao.getCourseByCourseName("测试", "");
        course.setCourseLocation("秦城监狱");
        courseDao.updateCourse(course, "");
    }

    @Test
    void testGetCourseByCourseName() {
        Course course = courseDao.getCourseByCourseName("测试", "");
        System.out.println(course);
    }

    @Test
    void testListAllByCourseDateCourses() {
        System.out.println(courseDao.listAllByCourseDateCourses("Sunday", ""));
    }

    @Test
    void testListAllCourses() {
        System.out.println(courseDao.listAllCourses(""));
    }
}
