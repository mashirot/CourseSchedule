package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Result saveCourse(Course course, String tableName) {
        try {
            StringBuilder date = new StringBuilder();
            for (String s : course.getCourseInputDate()) {
                date.append(s);
                date.append(s.equals(course.getCourseInputDate()[course.getCourseInputDate().length - 1]) ? "" : ",");
            }
            course.setCourseDate(date.toString());
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
            StringBuilder sb = new StringBuilder();
            for (String s : course.getCourseInputDate()) {
                sb.append(s).append(s.equals(course.getCourseInputDate()[course.getCourseInputDate().length - 1]) ? "" : ",");
            }
            course.setCourseDate(sb.toString());
            course.setJsonCourseWeek(objectMapper.writeValueAsString(course.getCourseWeek()));
        } catch (JsonProcessingException e) {
            return new Result(Code.UPDATE_COURSE_FAILED, null);
        }
        return new Result(courseDao.updateCourse(course, tableName) != 0 ? Code.UPDATE_COURSE_SUCCESS : Code.UPDATE_COURSE_FAILED, null);
    }

    @Override
    public Result getCourseByCourseName(String courseName, String tableName) {
        Course course = courseDao.getCourseByCourseName(courseName, tableName);
        List<Course> courseList = new ArrayList<>(5);
        try {
            String[] weeks = objectMapper.readValue(course.getJsonCourseWeek(), String[].class);
            int index = 0;
            if (course.getCourseDate().contains(",")) {
                String[] dates = course.getCourseDate().split(",");
                for (String date : dates) {
                    String[] s = date.split(" ");
                    Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index++]);
                    courseList.add(c);
                }
            } else {
                String[] s = course.getCourseDate().split(" ");
                courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index]));
            }

        } catch (JsonProcessingException e) {
            return new Result(Code.GET_SINGLE_FAILED, null);
        }
        return new Result(Code.GET_SINGLE_SUCCESS, courseList);
    }

    @Override
    public Result listAllByCourseDateCourses(String courseDate, String tableName) {
        List<Course> list = courseDao.listAllByCourseDateCourses(courseDate, tableName);
        List<Course> courseList = new ArrayList<>(list.size() * 2 + 1);
        for (Course course : list) {
            try {
                course.setCourseWeek(objectMapper.readValue(course.getJsonCourseWeek(), String[].class));
                String[] weeks = objectMapper.readValue(course.getJsonCourseWeek(), String[].class);
                if (course.getCourseDate().contains(",")) {
                    String[] dates = course.getCourseDate().split(",");
                    int index = 0;
                    for (String date : dates) {
                        String[] s = date.split(" ");
                        if (!s[0].equals(courseDate)) {
                            continue;
                        }
                        Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index++]);
                        courseList.add(c);
                    }
                } else {
                    int index = 0;
                    String[] s = course.getCourseDate().split(" ");
                    if (!s[0].equals(courseDate)) {
                        continue;
                    }
                    Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index]);
                    courseList.add(c);
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
                int index = 0;
                String[] weeks = objectMapper.readValue(course.getJsonCourseWeek(), String[].class);
                if (course.getCourseDate().contains(",")) {
                    String[] dates = course.getCourseDate().split(",");
                    for (String date : dates) {
                        String[] s = date.split(" ");
                        Course c = new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index++]);
                        courseList.add(c);
                    }
                } else {
                    String[] s = course.getCourseDate().split(" ");
                    courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index]));
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
                int index = 0;
                String[] weeks = objectMapper.readValue(course.getJsonCourseWeek(), String[].class);
                if (course.getCourseDate().contains(",")) {
                    String[] dates = course.getCourseDate().split(",");
                    for (String date : dates) {
                        String[] s = date.split(" ");
                        if (!s[0].equals(courseDate)) {
                            continue;
                        }
                        if (!isCourseEffect(initDate, weeks[index])) {
                            index++;
                            continue;
                        }
                        courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index++]));
                    }
                } else {
                    String[] s = course.getCourseDate().split(" ");
                    courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index]));
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
                int index = 0;
                String[] weeks = objectMapper.readValue(course.getJsonCourseWeek(), String[].class);
                if (course.getCourseDate().contains(",")) {
                    String[] dates = course.getCourseDate().split(",");
                    for (String date : dates) {
                        String[] s = date.split(" ");
                        if (!isCourseEffect(initDate, weeks[index])) {
                            index++;
                            continue;
                        }
                        courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index++]));
                    }
                } else {
                    if (!isCourseEffect(initDate, weeks[index])) {
                        continue;
                    }
                    String[] s = course.getCourseDate().split(" ");
                    courseList.add(new Course(course.getCourseName(), course.getCourseLocation(), course.getCourseLecturer(), s[0], s[1], weeks[index]));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(Code.LIST_ALL_FAILED, null);
            }
        }
        return new Result(Code.LIST_ALL_SUCCESS, courseList);
    }

    private boolean isCourseEffect(String initDate, String courseWeek) throws ParseException {
        String currentWeek = Utils.calcCurrentWeek(new Date(), Utils.transitionStrToDate(initDate));
        String[] str = courseWeek.split("-");
        return Integer.parseInt(currentWeek) >= Integer.parseInt(str[0]) && Integer.parseInt(currentWeek) <= Integer.parseInt(str[1]);
    }

}
