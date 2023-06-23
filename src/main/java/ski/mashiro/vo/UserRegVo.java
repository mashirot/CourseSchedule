package ski.mashiro.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
public class UserRegVo {
    private String username;
    private String password;
    private Date termStartDate;
    private Date termEndDate;
    private String captchaCode;
}
