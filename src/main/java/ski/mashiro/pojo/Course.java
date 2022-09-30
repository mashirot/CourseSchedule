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
}
