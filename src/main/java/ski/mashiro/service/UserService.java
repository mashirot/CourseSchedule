package ski.mashiro.service;

import ski.mashiro.pojo.User;
import ski.mashiro.vo.Result;

/**
 * @author MashiroT
 */
public interface UserService {
    Result<String> saveUser(User user);
    Result<String> updateUser(User user);
    Result<User> getUserByApiToken(User user);
    Result<User> getApiTokenByUsername(User user);
    Result<User> getUserByPassword(User user);
}
