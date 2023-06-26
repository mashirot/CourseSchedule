package ski.mashiro.service;

import ski.mashiro.pojo.User;
import ski.mashiro.dto.Result;
import ski.mashiro.vo.UserInfoVo;
import ski.mashiro.vo.UserLoginVo;
import ski.mashiro.vo.UserRegVo;

/**
 * @author MashiroT
 */
public interface UserService {
    Result<String> saveUser(UserRegVo user);
    Result<String> updateUser(User user);
    Result<User> getUserByApiToken(User user);
    Result<User> getApiTokenByUsername(User user);
    Result<User> getUserByPassword(UserLoginVo user);
    Result<UserInfoVo> getUserInfoByUsername(String username);
}
