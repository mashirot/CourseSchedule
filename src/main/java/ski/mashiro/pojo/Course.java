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
    private String courseDate;
    private Map<String, String> courseNormalDate;

    public Course() {
    }

    public Course(String courseName, String courseLocation, String courseLecturer, Map<String, String> courseNormalDate) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
        this.courseLecturer = courseLecturer;
        this.courseNormalDate = courseNormalDate;
    }
}
