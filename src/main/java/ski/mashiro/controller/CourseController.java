package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.vo.CourseVo;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.dto.Result;

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
    public Result<String> delCourse(@RequestBody CourseSearchVo courseSearchVo) {
        return courseService.delCourse(courseSearchVo);
    }

    @TokenRequired
    @PostMapping("/update")
    public Result<String> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    @TokenRequired
    @PostMapping("/eff")
    public Result<List<CourseVo>> listEffCourse(@RequestBody CourseSearchVo courseSearchVo) {
        return courseService.listCourseByCondition(courseSearchVo);
    }

    @TokenRequired
    @PostMapping("/sel")
    public Result<List<CourseVo>> listCourseByCondition(@RequestBody CourseSearchVo courseSearchVo) {
        return courseService.listCourseByCondition(courseSearchVo);
    }

    @TokenRequired
    @PostMapping("/all")
    public Result<List<CourseVo>> listCourse(@RequestBody CourseSearchVo courseSearchVo) {
        return courseService.listCourse(courseSearchVo);
    }
}
