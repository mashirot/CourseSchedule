package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ski.mashiro.pojo.Course;

import java.util.List;

/**
* @author MashiroT
*/
@Mapper
public interface CourseDao {
    int saveCourse(@Param("course") Course course);
    int deleteByCourseName(@Param("courseName") String courseName);
    int updateCourse(@Param("course") Course course);
    Course getCourseByCourseName(@Param("courseName") String courseName);
    List<Course> listAllByCourseDateCourses(@Param("courseDate") String courseDate);
    List<Course> listAllCourses();
}
