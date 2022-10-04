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
    private String courseWeek;
    private String courseDate;
    private Map<String, String> courseNormalDate;
    private String courseShowDate;
    private String courseShowTime;
    private String[] courseInputDate;
    public Course() {
    }
    public Course(String courseName, String courseLocation, String courseLecturer, String courseShowDate, String courseShowTime) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
        this.courseLecturer = courseLecturer;
        this.courseShowDate = courseShowDate;
        this.courseShowTime = courseShowTime;
    }
}
