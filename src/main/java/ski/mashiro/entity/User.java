package ski.mashiro.entity;

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
public class User {
    private Integer uid;
    private String username;
    private String password;
    private Date termStartDate;
    private Date termEndDate;
    private String apiToken;

    public User(String username, String password, Date termStartDate, Date termEndDate) {
        this.username = username;
        this.password = password;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
