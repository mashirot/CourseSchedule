package ski.mashiro.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.dto.Result;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.vo.CourseVo;

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
        var username = (String) request.getSession().getAttribute("username");
        Integer uid = getUid(username);
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
        var username = (String) request.getSession().getAttribute("username");
        Integer uid = getUid(username);
        Result<String> result = courseService.saveCourseInFile(courseFile, uid);
        if (result.code() == StatusCodeConstants.COURSE_INSERT_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/del")
    public Result<String> delCourse(HttpServletRequest request, @RequestBody CourseSearchVo courseSearchVo) {
        var username = (String) request.getSession().getAttribute("username");
        Integer uid = getUid(username);
        courseSearchVo.setUid(uid);
        Result<String> result = courseService.delCourse(courseSearchVo);
        if (result.code() == StatusCodeConstants.COURSE_DELETE_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/update")
    public Result<String> updateCourse(HttpServletRequest request, @RequestBody Course course) {
        var username = (String) request.getSession().getAttribute("username");
        Integer uid = getUid(username);
        course.setUid(uid);
        Result<String> result = courseService.updateCourse(course);
        if (result.code() == StatusCodeConstants.COURSE_UPDATE_SUCCESS) {
            stringRedisTemplate.delete(COURSE_KEY + uid + COURSE_ALL);
        }
        return result;
    }

    @TokenRequired
    @PostMapping("/sel")
    public Result<List<CourseVo>> listCourseByCondition(HttpServletRequest request, @RequestBody CourseSearchVo courseSearchVo) {
        var username = (String) request.getSession().getAttribute("username");
        return courseService.listCourseByCondition(username, courseSearchVo);
    }

    private Integer getUid(String username) {
        return Integer.parseInt(stringRedisTemplate.opsForValue().get(USER_KEY + username + USER_UID));
    }
}
