package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ski.mashiro.bo.CourseSearchBo;
import ski.mashiro.dto.CourseSearchDTO;
import ski.mashiro.entity.Course;

import java.util.List;

/**
 * @author MashiroT
 */
@Mapper
public interface CourseDao {
    int insertCourse(@Param("course") Course course);
    int delCourseByCondition(@Param("course") CourseSearchDTO course);
    int updateCourse(@Param("course") Course course);
    List<Course> listCourseByCondition(@Param("course") CourseSearchBo course);
}
