package ski.mashiro.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author FeczIne
 */
@Data
public class User {
    private String userCode;
    private String userPassword;
    private String userNickname;
    private Date termInitialDate;
    private Date currentDate;
    private String currentWeek;
}
