package ski.mashiro.service;

import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.bo.CourseSearchBo;
import ski.mashiro.vo.CourseVo;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.pojo.Course;
import ski.mashiro.dto.Result;

import java.util.List;

/**
 * @author MashiroT
 */
public interface CourseService {
    Result<String> saveCourse(Course course);
    Result<String> saveCourseInFile(MultipartFile courseFile, int uid);
    Result<String> delCourse(CourseSearchVo courseSearchVo);
    Result<String> updateCourse(Course course);
    Result<List<CourseVo>> listCourseByCondition(CourseSearchVo courseSearchVo);
    Result<List<CourseVo>> listCourse(CourseSearchBo courseSearchBo);
}
