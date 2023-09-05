package ski.mashiro.service;

import ski.mashiro.entity.User;
import ski.mashiro.common.Result;
import ski.mashiro.dto.UserInfoDTO;
import ski.mashiro.dto.UserLoginDTO;
import ski.mashiro.dto.UserRegDTO;

/**
 * @author MashiroT
 */
public interface UserService {
    Result<String> saveUser(UserRegDTO user);
    Result<String> updateUser(User user);
    Result<User> getUserByApiToken(User user);
    Result<UserLoginDTO> getUserByPassword(UserLoginDTO user);
    Result<UserInfoDTO> getUserInfoByUsername(int uid);
}
