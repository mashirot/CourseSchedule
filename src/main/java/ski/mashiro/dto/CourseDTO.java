package ski.mashiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Integer courseId;
    private String dayOfWeek;
    private String time;
    private String name;
    private String place;
    private String teacher;
    private String week;
    private String oddWeek;
    private Double credit;
}
