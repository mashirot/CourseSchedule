package ski.mashiro.vo;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class UserLoginVo {
    private String username;
    private String password;
    private String apiToken;
    private String authToken;

    public UserLoginVo(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }
}
