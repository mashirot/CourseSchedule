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
    int saveCourse(@Param("course") Course course, @Param("tableName") String tableName);
    int deleteByCourseName(@Param("courseName") String courseName, @Param("tableName") String tableName);
    int updateCourse(@Param("course") Course course, @Param("tableName") String tableName);
    Course getCourseByCourseName(@Param("courseName") String courseName, @Param("tableName") String tableName);
    List<Course> listAllByCourseDateCourses(@Param("courseDate") String courseDate, @Param("tableName") String tableName);
    List<Course> listAllCourses(@Param("tableName") String tableName);
}
