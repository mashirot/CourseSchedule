package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.service.CourseService;

/**
 * @author FeczIne
 */
@RestController
@RequestMapping("/schedules")
public class CourseController {
    private final CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    @PostMapping
    public Result saveCourse(@RequestBody Course course, @CookieValue(value = "userTableName") String userTableName) {
        System.out.println(course);
        return courseService.saveCourse(course, userTableName);
    }
    @DeleteMapping("/{courseName}")
    public Result deleteCourse(@PathVariable String courseName, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.deleteByCourseName(courseName, userTableName);
    }
    @PutMapping
    public Result updateCourse(@RequestBody Course course, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.updateCourse(course, userTableName);
    }
    @GetMapping("/courseName/{courseName}")
    public Result getCourseByCourseName(@PathVariable String courseName, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.getCourseByCourseName(courseName, userTableName);
    }
    @GetMapping("/courseDate/all/{courseDate}")
    public Result listAllByCourseDateCourses(@PathVariable String courseDate, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.listAllByCourseDateCourses(courseDate, userTableName);
    }
    @GetMapping("/all")
    public Result listAllCourses(@CookieValue(value = "userTableName") String userTableName) {
        return courseService.listAllCourses(userTableName);
    }
    @GetMapping("/courseDate/{courseDate}")
    public Result listAllEffectiveByCourseDateCourses(@PathVariable String courseDate, @CookieValue("initDate") String initDate, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.listAllEffectiveByCourseDateCourses(courseDate, initDate, userTableName);
    }
    @GetMapping
    public Result listAllEffectiveCourses(@CookieValue("initDate") String initDate, @CookieValue(value = "userTableName") String userTableName) {
        return courseService.listAllEffectiveCourses(initDate, userTableName);
    }
}
