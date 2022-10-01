package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import ski.mashiro.dao.CourseDao;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author MashiroT
* 2022-09-30 14:55:19
*/
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Result saveCourse(Course course) {
        try {
            course.setCourseDate(objectMapper.writeValueAsString(course.getCourseNormalDate()));
        } catch (JsonProcessingException e) {
            return new Result(Code.SAVE_FAILED, null);
        }
        return new Result(courseDao.saveCourse(course) != 0 ? Code.SAVE_SUCCESS : Code.SAVE_FAILED, null);
    }

    @Override
    public Result deleteByCourseName(String courseName) {
        return new Result(courseDao.deleteByCourseName(courseName) != 0 ? Code.DELETE_SUCCESS : Code.DELETE_FAILED, null);
    }

    @Override
    public Result updateCourse(Course course) {
        return new Result(courseDao.updateCourse(course) != 0 ? Code.UPDATE_SUCCESS : Code.UPDATE_FAILED, null);
    }

    @Override
    public Result getCourseByCourseName(String courseName) {
        Course course = courseDao.getCourseByCourseName(courseName);
        try {
            course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
        } catch (JsonProcessingException e) {
            return new Result(Code.GET_SINGLE_FAILED, null);
        }
        return new Result(Code.GET_SINGLE_SUCCESS, course);
    }

    @Override
    public Result listAllByCourseDateCourses(String courseDate) {
        List<Course> list = courseDao.listAllByCourseDateCourses(courseDate);
        List<Course> courseList = new ArrayList<>(list.size());
        for (Course course : list) {
            try {
                Map<String, String> currentDate = objectMapper.readValue(course.getCourseDate(), mapType);
                for (Map.Entry<String, String> entry : currentDate.entrySet()) {
                    if (!courseDate.equals(entry.getKey())) {
                        currentDate.remove(entry.getKey(), entry.getValue());
                    }
                }
                courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), currentDate));
            } catch (JsonProcessingException e) {
                return new Result(Code.LIST_DATE_FAILED, null);
            }
        }
        return new Result(Code.LIST_DATE_SUCCESS, courseList);
    }

    @Override
    public Result listAllCourses() {
        List<Course> list = courseDao.listAllCourses();
        for (Course course : list) {
            try {
                course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
            } catch (JsonProcessingException e) {
                return new Result(Code.LIST_ALL_FAILED, null);
            }
        }
        return new Result(Code.LIST_ALL_SUCCESS, list);
    }
}
