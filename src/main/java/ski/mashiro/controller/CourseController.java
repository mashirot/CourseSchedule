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
    public Result saveCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }
    @DeleteMapping("/{courseName}")
    public Result deleteCourse(@PathVariable String courseName) {
        return courseService.deleteByCourseName(courseName);
    }
    @PutMapping
    public Result updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }
    @GetMapping("/courseName/{courseName}")
    public Result getCourseByCourseName(@PathVariable String courseName) {
        return courseService.getCourseByCourseName(courseName);
    }
    @GetMapping("/courseDate/{courseDate}")
    public Result listAllByCourseDateCourses(@PathVariable String courseDate) {
        return courseService.listAllByCourseDateCourses(courseDate);
    }
    @GetMapping
    public Result listAllCourses() {
        return courseService.listAllCourses();
    }
}
