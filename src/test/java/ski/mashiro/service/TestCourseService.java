package ski.mashiro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestCourseService {

    @Autowired
    private CourseService courseService;
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
        System.out.println((courseService.saveCourse(course, "")));
    }

    @Test
    void testDeleteByCourseName() {
        System.out.println(courseService.deleteByCourseName("测试", ""));
    }

    @Test
    void testUpdateCourse() {
        Result rs = courseService.getCourseByCourseName("测试", "");
        Course data = (Course) rs.getData();
        data.setCourseLocation("秦城监狱");
        Result result = courseService.updateCourse(data, "");
        System.out.println(result);
    }

    @Test
    void testGetCourseByCourseName() {
        Result course = courseService.getCourseByCourseName("测试", "");
        Course data = (Course) course.getData();
        System.out.println(data.getCourseNormalDate());
    }

    @Test
    void testListAllByCourseDateCourses() {
        System.out.println(courseService.listAllByCourseDateCourses("Sunday", ""));
    }

    @Test
    void testListAllCourses() {
        System.out.println(courseService.listAllCourses(""));
    }
}
