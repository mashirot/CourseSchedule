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
    private String passwordSalt;
    private String userNickname;
    private Date termInitialDate;
    private String currentWeek;
    private String userTableName;
    private String userApiToken;
}
