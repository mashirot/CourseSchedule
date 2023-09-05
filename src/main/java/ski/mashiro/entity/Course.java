package ski.mashiro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Integer uid;
    private Integer courseId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String name;
    private String place;
    private String teacher;
    private Integer startWeek;
    private Integer endWeek;
    private Integer oddWeek;
    private Double credit;
}
