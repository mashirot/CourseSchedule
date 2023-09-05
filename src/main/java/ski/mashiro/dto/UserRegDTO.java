package ski.mashiro.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
public class UserRegDTO {
    private String username;
    private String password;
    private Date termStartDate;
    private Date termEndDate;
    private String captchaCode;
}
