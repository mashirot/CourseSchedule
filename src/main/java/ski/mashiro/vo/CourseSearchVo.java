package ski.mashiro.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class CourseSearchVo {
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

    public CourseSearchVo(Integer uid) {
        this.uid = uid;
    }

    public CourseSearchVo(Integer uid, Date termStartDate) {
        this.uid = uid;
        this.termStartDate = termStartDate;
    }
}
