package ski.mashiro.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.common.Result;
import ski.mashiro.entity.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.dto.CourseSearchDTO;
import ski.mashiro.dto.CourseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ski.mashiro.constant.RedisKeyConstant.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/sched")
public class CourseController {

    private final CourseService courseService;
    private final StringRedisTemplate stringRedisTemplate;

    public CourseController(CourseService courseService, StringRedisTemplate stringRedisTemplate) {
        this.courseService = courseService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @TokenRequired
    @PostMapping("/ins")
    public Result<String> saveCourse(HttpServletRequest request, @RequestBody Course course) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        course.setUid(uid);
        Result<String> result = courseService.saveCourse(course);
        if (result.code() == StatusCodeConstants.COURSE_INSERT_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/file")
    public Result<String> uploadFile(@RequestBody MultipartFile courseFile, HttpServletRequest request) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        Result<String> result = courseService.saveCourseInFile(courseFile, uid);
        if (result.code() == StatusCodeConstants.COURSE_INSERT_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/del")
    public Result<String> delCourse(HttpServletRequest request, @RequestBody CourseSearchDTO courseSearchDTO) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        courseSearchDTO.setUid(uid);
        Result<String> result = courseService.delCourse(courseSearchDTO);
        if (result.code() == StatusCodeConstants.COURSE_DELETE_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/update")
    public Result<String> updateCourse(HttpServletRequest request, @RequestBody Course course) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        course.setUid(uid);
        Result<String> result = courseService.updateCourse(course);
        if (result.code() == StatusCodeConstants.COURSE_UPDATE_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/sel")
    public Result<List<CourseDTO>> listCourseByCondition(HttpServletRequest request, @RequestBody CourseSearchDTO courseSearchDTO) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        courseSearchDTO.setUid(uid);
        return courseService.listCourseByCondition(courseSearchDTO);
    }
}
