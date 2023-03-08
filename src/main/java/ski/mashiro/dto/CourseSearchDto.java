package ski.mashiro.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
public class CourseSearchDto {
    private Integer uid;
    private Integer courseId;
    private Integer currWeek;
    private String dayOfWeek;
    private Date termStartDate;
}
