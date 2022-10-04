package ski.mashiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ski.mashiro.dao.UserDao;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;

import java.util.Date;

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
    public Result saveUser(User user) {
        user.setCurrentWeek(calcCurrentWeek(user.getCurrentDate(), user.getTermInitialDate()));
        return new Result(userDao.saveUser(user) == 1 ? Code.SAVE_USER_SUCCESS : Code.SAVE_USER_FAILED, null);
    }

    @Override
    public Result deleteUser(String userCode, String userPasswd) {
        return new Result(userDao.deleteUser(userCode, userPasswd) == 1 ? Code.DELETE_USER_SUCCESS : Code.DELETE_USER_FAILED, null);
    }

    @Override
    public Result updateCurrentWeek(String userCode, Date currentDate) {
        int rs = userDao.updateCurrentDate(userCode, currentDate, calcCurrentWeek(currentDate, userDao.getInitDate(userCode)));
        return new Result(rs == 1 ? Code.UPDATE_USER_CURRENT_WEEK_SUCCESS : Code.UPDATE_USER_CURRENT_WEEK_FAILED, null);
    }

    @Override
    public Result updateUserPassword(String userCode, String userPassword) {
        return new Result(userDao.updateUserPassword(userCode, userPassword) == 1 ? Code.UPDATE_USER_PASSWORD_SUCCESS : Code.UPDATE_USER_PASSWORD_FAILED, null);
    }

    @Override
    public Result getUser(String userCode, String userPasswd) {
        User user = userDao.getUser(userCode, userPasswd);
        return new Result(user != null ? Code.GET_USER_SUCCESS : Code.GET_USER_FAILED, user);
    }

    private String calcCurrentWeek(Date currentDate, Date initDate) {
        return String.valueOf((currentDate.getTime() - initDate.getTime() + 7 * 24 * 60 * 60 * 1000) / (7 * 24 * 60 * 60 * 1000) + 1);
    }
}
