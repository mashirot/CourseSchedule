package ski.mashiro.service;

import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.pojo.Course;
import ski.mashiro.vo.Result;

import java.util.List;

/**
 * @author MashiroT
 */
public interface CourseService {
    Result<String> saveCourse(Course course);
    Result<String> saveCourseInFile(MultipartFile courseFile, int uid);
    Result<String> delCourse(CourseSearchDto courseSearchDto);
    Result<String> updateCourse(Course course);
    Result<List<CourseDto>> listCourseByCondition(CourseSearchDto courseSearchDto);
    Result<List<CourseDto>> listCourse(CourseSearchDto courseSearchDto);
}
