package ski.mashiro.pojo;

import lombok.Data;

import java.util.Map;

/**
 * @author MashiroT
 * 2022-09-30 14:52:11
 */
@Data
public class Course {
    private String courseName;
    private String courseLocation;
    private String courseLecturer;
    private String[] courseWeek;
    private String jsonCourseWeek;
    private String courseDate;
    private Map<String, String> courseNormalDate;
    private String courseShowDate;
    private String courseShowTime;
    private String courseShowWeek;
    private String[] courseInputDate;
    public Course() {
    }
    public Course(String courseName, String courseLocation, String courseLecturer, String courseShowDate, String courseShowTime, String courseShowWeek) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
        this.courseLecturer = courseLecturer;
        this.courseShowDate = courseShowDate;
        this.courseShowTime = courseShowTime;
        this.courseShowWeek = courseShowWeek;
    }
}
