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
import java.util.Date;
import java.util.List;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/sched")
public class CourseController {

    public final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @TokenRequired
    @PostMapping("/ins")
    public Result<String> saveCourse(HttpServletRequest request, @RequestBody Course course) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        course.setUid(uid);
        return courseService.saveCourse(course);
    }

    @TokenRequired
    @PostMapping("/file")
    public Result<String> uploadFile(@RequestBody MultipartFile courseFile, HttpServletRequest req) {
        return courseService.saveCourseInFile(courseFile, Integer.parseInt(req.getParameter("uid")));
    }

    @TokenRequired
    @PostMapping("/del")
    public Result<String> delCourse(HttpServletRequest request, @RequestBody CourseSearchVo courseSearchVo) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        courseSearchVo.setUid(uid);
        return courseService.delCourse(courseSearchVo);
    }

    @TokenRequired
    @PostMapping("/update")
    public Result<String> updateCourse(HttpServletRequest request, @RequestBody Course course) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        course.setUid(uid);
        return courseService.updateCourse(course);
    }

    @TokenRequired
    @PostMapping("/sel")
    public Result<List<CourseVo>> listCourseByCondition(HttpServletRequest request, @RequestBody CourseSearchVo courseSearchVo) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        Date termStartDate = (Date) request.getSession().getAttribute("termStartDate");
        courseSearchVo.setUid(uid);
        courseSearchVo.setTermStartDate(termStartDate);
        return courseService.listCourseByCondition(courseSearchVo);
    }
}
