package ski.mashiro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String username;
    private String password;
    private String apiToken;
    private String authToken;

    public UserLoginDTO(String authToken) {
        this.authToken = authToken;
    }
}
