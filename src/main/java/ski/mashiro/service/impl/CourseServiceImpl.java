package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.dao.CourseDao;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.service.CourseService;
import org.springframework.stereotype.Service;
import ski.mashiro.util.Utils;

import java.text.ParseException;
import java.util.*;

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
    public Result saveCourse(Course course, String tableName) {
        try {
            Map<String, String> map = new HashMap<>(course.getCourseInputDate().length);
            for (String s : course.getCourseInputDate()) {
                String[] rs = s.split(" ");
                map.put(rs[0], rs[1]);
            }
            course.setCourseNormalDate(map);
            course.setCourseDate(objectMapper.writeValueAsString(course.getCourseNormalDate()));
            course.setJsonCourseWeek(objectMapper.writeValueAsString(course.getCourseWeek()));
        } catch (JsonProcessingException e) {
            return new Result(Code.SAVE_COURSE_FAILED, null);
        }
        return new Result(courseDao.saveCourse(course, tableName) != 0 ? Code.SAVE_COURSE_SUCCESS : Code.SAVE_COURSE_FAILED, null);
    }

    @Override
    public Result saveCoursesFromFile(MultipartFile multipartFile, String tableName) {
        List<Course> courses = Utils.analyzeScheduleFile(multipartFile);
        if (courses == null) {
            return new Result(Code.FILE_ANALYZE_FAILED, null);
        }
        try {
            for (Course course : courses) {
                course.setCourseDate(objectMapper.writeValueAsString(course.getCourseNormalDate()));
                course.setJsonCourseWeek(objectMapper.writeValueAsString(course.getCourseWeek()));
                courseDao.saveCourse(course, tableName);
            }
        } catch (Exception e) {
            return new Result(Code.FILE_ANALYZE_FAILED, null);
        }
        return new Result(Code.FILE_ANALYZE_SUCCESS, null);
    }

    @Override
    public Result deleteByCourseName(String courseName, String tableName) {
        return new Result(courseDao.deleteByCourseName(courseName, tableName) != 0 ? Code.DELETE_COURSE_SUCCESS : Code.DELETE_COURSE_FAILED, null);
    }

    @Override
    public Result updateCourse(Course course, String tableName) {
        try {
            course.setCourseDate(objectMapper.writeValueAsString(course.getCourseNormalDate()));
        } catch (JsonProcessingException e) {
            return new Result(Code.UPDATE_COURSE_FAILED, null);
        }
        return new Result(courseDao.updateCourse(course, tableName) != 0 ? Code.UPDATE_COURSE_SUCCESS : Code.UPDATE_COURSE_FAILED, null);
    }

    @Override
    public Result getCourseByCourseName(String courseName, String tableName) {
        Course course = courseDao.getCourseByCourseName(courseName, tableName);
        try {
            course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
        } catch (JsonProcessingException e) {
            return new Result(Code.GET_SINGLE_FAILED, null);
        }
        return new Result(Code.GET_SINGLE_SUCCESS, course);
    }

    @Override
    public Result listAllByCourseDateCourses(String courseDate, String tableName) {
        List<Course> list = courseDao.listAllByCourseDateCourses(courseDate, tableName);
        List<Course> courseList = new ArrayList<>(list.size() * 2 + 1);
        for (Course course : list) {
            try {
                course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
                course.setCourseWeek(objectMapper.readValue(course.getJsonCourseWeek(), String[].class));
                int index = 0;
                for (Map.Entry<String, String> entry : course.getCourseNormalDate().entrySet()) {
                    Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), entry.getKey(), entry.getValue(), course.getCourseWeek()[index++]);
                    if (courseDate.equals(c.getCourseShowDate())) {
                        courseList.add(c);
                    }
                }
            } catch (JsonProcessingException e) {
                return new Result(Code.LIST_DATE_FAILED, null);
            }
        }
        return new Result(Code.LIST_DATE_SUCCESS, courseList);
    }

    @Override
    public Result listAllCourses(String tableName) {
        List<Course> list = courseDao.listAllCourses(tableName);
        List<Course> courseList = new ArrayList<>(list.size() * 2 + 1);
        for (Course course : list) {
            try {
                course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
                course.setCourseWeek(objectMapper.readValue(course.getJsonCourseWeek(), String[].class));
                int index = 0;
                for (Map.Entry<String, String> entry : course.getCourseNormalDate().entrySet()) {
                    courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), entry.getKey(), entry.getValue(), course.getCourseWeek()[index++]));
                }
            } catch (Exception e) {
                return new Result(Code.LIST_ALL_FAILED, null);
            }
        }
        return new Result(Code.LIST_ALL_SUCCESS, courseList);
    }

    @Override
    public Result listAllEffectiveByCourseDateCourses(String courseDate, String initDate, String tableName) {
        List<Course> list = courseDao.listAllByCourseDateCourses(courseDate, tableName);
        List<Course> courseList = new ArrayList<>(list.size() * 2 + 1);
        for (Course course : list) {
            try {
                course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
                course.setCourseWeek(objectMapper.readValue(course.getJsonCourseWeek(), String[].class));
                int index = 0;
                for (Map.Entry<String, String> entry : course.getCourseNormalDate().entrySet()) {
                    Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), entry.getKey(), entry.getValue(), course.getCourseWeek()[index++]);
                    if (courseDate.equals(c.getCourseShowDate()) && isCourseEffect(initDate, c)) {
                        courseList.add(c);
                    }
                }
            } catch (Exception e) {
                return new Result(Code.LIST_DATE_FAILED, null);
            }
        }
        return new Result(Code.LIST_DATE_SUCCESS, courseList);
    }

    @Override
    public Result listAllEffectiveCourses(String initDate, String tableName) {
        List<Course> list = courseDao.listAllCourses(tableName);
        List<Course> courseList = new ArrayList<>(list.size() * 2 + 1);
        for (Course course : list) {
            try {
                course.setCourseNormalDate(objectMapper.readValue(course.getCourseDate(), mapType));
                course.setCourseWeek(objectMapper.readValue(course.getJsonCourseWeek(), String[].class));
                int index = 0;
                for (Map.Entry<String, String> entry : course.getCourseNormalDate().entrySet()) {
                    Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), entry.getKey(), entry.getValue(), course.getCourseWeek()[index++]);
                    if (isCourseEffect(initDate, c)) {
                        courseList.add(c);
                    }
                }
            } catch (Exception e) {
                return new Result(Code.LIST_ALL_FAILED, null);
            }
        }
        return new Result(Code.LIST_ALL_SUCCESS, courseList);
    }

    private boolean isCourseEffect(String initDate, Course course) throws ParseException {
        String currentWeek = Utils.calcCurrentWeek(new Date(), Utils.transitionStrToDate(initDate));
        String courseWeek = course.getCourseShowWeek();
        String[] str = courseWeek.split("-");
        return Integer.parseInt(currentWeek) >= Integer.parseInt(str[0]) && Integer.parseInt(currentWeek) <= Integer.parseInt(str[1]);
    }

}
