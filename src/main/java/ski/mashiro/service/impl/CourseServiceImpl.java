package ski.mashiro.service.impl;

import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MashiroT
* 2022-09-30 14:55:19
*/
@Service
public class CourseServiceImpl implements CourseService {

    @Override
    public boolean saveCourse(Course course) {
        return false;
    }

    @Override
    public boolean deleteByCourseName(String courseName) {
        return false;
    }

    @Override
    public boolean updateCourse(Course course) {
        return false;
    }

    @Override
    public Course getCourseByCourseName(String courseName) {
        return null;
    }

    @Override
    public List<Course> listAllByCourseDateCourses(String courseDate) {
        return null;
    }

    @Override
    public List<Course> listAllCourses() {
        return null;
    }
}
