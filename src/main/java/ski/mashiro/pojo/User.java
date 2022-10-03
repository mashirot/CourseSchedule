package ski.mashiro.pojo;

import lombok.Data;

/**
 * @author FeczIne
 */
@Data
public class User {
    private String userType;
    private String userCode;
    private String termInitialTime;
    private String currentWeek;
}
