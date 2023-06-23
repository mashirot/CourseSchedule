package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.pojo.Course;

import java.util.List;

/**
 * @author MashiroT
 */
@Mapper
public interface CourseDao {
    int insertCourse(@Param("course") Course course);
    int delCourseByCondition(@Param("course") CourseSearchVo course);
    int updateCourse(@Param("course") Course course);
    List<Course> listCourseByCondition(@Param("course") CourseSearchVo course);
}
