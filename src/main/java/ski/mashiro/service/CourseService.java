package ski.mashiro.service;

import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.bo.CourseSearchBo;
import ski.mashiro.dto.CourseDTO;
import ski.mashiro.dto.CourseSearchDTO;
import ski.mashiro.entity.Course;
import ski.mashiro.common.Result;

import java.util.List;

/**
 * @author MashiroT
 */
public interface CourseService {
    Result<String> saveCourse(Course course);
    Result<String> saveCourseInFile(MultipartFile courseFile, int uid);
    Result<String> delCourse(CourseSearchDTO courseSearchDTO);
    Result<String> updateCourse(Course course);
    Result<List<CourseDTO>> listCourseByCondition(CourseSearchDTO courseSearchDTO);
    Result<List<CourseDTO>> listCourseByCondition4Api(CourseSearchDTO courseSearchDTO);
    Result<List<CourseDTO>> listCourse(CourseSearchBo courseSearchBo);
}
