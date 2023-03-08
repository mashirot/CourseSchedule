package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.pojo.User;
import ski.mashiro.service.CourseService;
import ski.mashiro.service.UserService;
import ski.mashiro.vo.Result;

import java.util.List;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public ApiController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping("/sel")
    public Result<List<CourseDto>> listCourseByCondition(@RequestBody CourseSearchDto courseSearchDto) {
        return courseService.listCourseByCondition(courseSearchDto);
    }

    @PostMapping("/login")
    public Result<User> loginByApiToken(@RequestBody User user) {
        return userService.getUserByApiToken(user);
    }
}
