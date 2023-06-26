package ski.mashiro.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ski.mashiro.dao.UserDao;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.dto.Result;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.vo.UserInfoVo;
import ski.mashiro.vo.UserLoginVo;
import ski.mashiro.vo.UserRegVo;

import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Result<String> saveUser(UserRegVo userReg) {
        var user = new User(userReg.getUsername(), userReg.getPassword(), userReg.getTermStartDate(), userReg.getTermEndDate());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setApiToken(DigestUtils.md5DigestAsHex(BCrypt.hashpw(user.getUsername() + user.getTermStartDate() + user.getTermEndDate(), BCrypt.gensalt()).getBytes()));
        if (userDao.saveUser(user) == 1) {
            return Result.success(USER_REG_SUCCESS, null);
        }
        return Result.failed(USER_REG_FAILED, null);
    }

    @Override
    public Result<User> getUserByPassword(UserLoginVo userLogin) {
        var user = new User(userLogin.getUsername(), userLogin.getPassword());
        var currUser = userDao.getUserByUsername(user.getUsername());
        boolean checkPw = BCrypt.checkpw(user.getPassword(), currUser.getPassword());
        if (checkPw) {
            return Result.success(USER_LOGIN_SUCCESS, currUser);
        }
        return Result.failed(USER_LOGIN_FAILED, null);
    }

    @Override
    public Result<UserInfoVo> getUserInfoByUsername(String username) {
        var user = userDao.getUserByUsername(username);
        int currWeek = WeekUtils.getCurrWeek(user.getTermStartDate());
        return new Result<>(USER_INFO_SUCCESS, new UserInfoVo(user.getUsername(), user.getTermStartDate(), user.getTermEndDate(), currWeek, user.getApiToken()), null);
    }

    @Override
    public Result<User> getUserByApiToken(User user) {
        User rsUser = userDao.getUserByApiToken(user.getUsername(), user.getApiToken());
        if (rsUser != null) {
            return Result.success(USER_LOGIN_SUCCESS, rsUser);
        }
        return Result.failed(USER_LOGIN_FAILED, null);
    }

    @Override
    public Result<User> getApiTokenByUsername(User user) {
        String token = userDao.getApiTokenByUsername(user.getUsername());
        if (token == null) {
            return Result.failed(USER_GET_API_FAILED, null);
        }
        user.setApiToken(token);
        return Result.success(USER_GET_API_SUCCESS, user);
    }

    @Override
    public Result<String> updateUser(User user) {
        try {
            if (user.getPassword() != null) {
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            }
            int rs = userDao.updateUser(user);
            if (rs == 1) {
                return Result.success(USER_MODIFY_SUCCESS, null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Result.failed(USER_MODIFY_FAILED, null);
    }
}
