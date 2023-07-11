package ski.mashiro.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class UserLoginVo {
    private String username;
    private String password;
    private String apiToken;
    private String authToken;

    public UserLoginVo(String authToken) {
        this.authToken = authToken;
    }
}
