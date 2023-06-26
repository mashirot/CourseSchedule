package ski.mashiro.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@AllArgsConstructor
public class UserInfoVo {
    private String username;
    private Date termStartDate;
    private Date termEndDate;
    private Integer currWeek;
    private String apiToken;
}
