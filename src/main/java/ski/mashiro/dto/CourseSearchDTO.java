package ski.mashiro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class CourseSearchDTO {
    private Integer uid;
    private Integer courseId;
    private String name;
    private String place;
    private Integer currWeek;
    private String dayOfWeek;
    private Boolean isEffective;
    private Double credit;
    private Integer oddWeek;
    private Date termStartDate;

    public CourseSearchDTO(Integer uid, String dayOfWeek, Boolean isEffective) {
        this.uid = uid;
        this.dayOfWeek = dayOfWeek;
        this.isEffective = isEffective;
    }
}
