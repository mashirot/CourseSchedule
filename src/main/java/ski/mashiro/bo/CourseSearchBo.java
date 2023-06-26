package ski.mashiro.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSearchBo {
    private Integer uid;
    private String name;
    private String place;
    private Integer currWeek;
    private String dayOfWeek;
    private Double credit;
    private Integer oddWeek;
}
