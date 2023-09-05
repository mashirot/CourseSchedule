package ski.mashiro.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private Date termStartDate;
    private Date termEndDate;
    private Integer currWeek;
    private String apiToken;
}
