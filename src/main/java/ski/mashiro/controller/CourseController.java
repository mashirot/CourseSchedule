package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author MashiroT
 */
@RestController
@CrossOrigin
@RequestMapping("/sched")
public class CourseController {

    public final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @TokenRequired
    @PostMapping("/ins")
    public Result<String> saveCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }

    @TokenRequired
    @PostMapping("/file")
    public Result<String> uploadFile(@RequestBody MultipartFile courseFile, HttpServletRequest req) {
        return courseService.saveCourseInFile(courseFile, Integer.parseInt(req.getParameter("uid")));
    }

    @TokenRequired
    @PostMapping("/del")
    public Result<String> delCourse(@RequestBody CourseSearchDto courseSearchDto) {
        return courseService.delCourse(courseSearchDto);
    }

    @TokenRequired
    @PostMapping("/update")
    public Result<String> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    @TokenRequired
    @PostMapping("/eff")
    public Result<List<CourseDto>> listEffCourse(@RequestBody CourseSearchDto courseSearchDto) {
        return courseService.listCourseByCondition(courseSearchDto);
    }

    @TokenRequired
    @PostMapping("/sel")
    public Result<List<CourseDto>> listCourseByCondition(@RequestBody CourseSearchDto courseSearchDto) {
        return courseService.listCourseByCondition(courseSearchDto);
    }

    @TokenRequired
    @PostMapping("/all")
    public Result<List<CourseDto>> listCourse(@RequestBody CourseSearchDto courseSearchDto) {
        return courseService.listCourse(courseSearchDto);
    }
}
