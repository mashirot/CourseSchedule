package ski.mashiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ski.mashiro.dao.TableDao;
import ski.mashiro.dao.UserDao;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.Utils;

import java.util.Date;

/**
 * @author MashiroT
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final TableDao tableDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, TableDao tableDao) {
        this.userDao = userDao;
        this.tableDao = tableDao;
    }

    @Override
    public Result saveUser(User user) {
        user.setUserTableName(Utils.transitionTableName(user.getUserCode()));
        int rs = userDao.saveUser(user);
        if (rs == 1) {
            tableDao.createTable(user.getUserTableName());
            return new Result(Code.SAVE_USER_SUCCESS, null);
        }
        return new Result(Code.SAVE_USER_FAILED, null);
    }

    @Override
    public Result deleteUser(String userCode) {
        int rs = userDao.deleteUser(userCode);
        if (rs == 1) {
            tableDao.deleteTable(Utils.transitionTableName(userCode));
            return new Result(Code.DELETE_USER_SUCCESS, null);
        }
        return new Result(Code.DELETE_USER_FAILED, null);
    }

    @Override
    public Result updateUser(User user) {
        return new Result(userDao.updateUser(user) == 1 ? Code.UPDATE_USER_SUCCESS : Code.UPDATE_USER_FAILED, null);
    }

    @Override
    public Result getInitDate(String userCode) {
        Date initDate = userDao.getInitDate(userCode);
        return new Result(initDate != null ? Code.GET_USER_INIT_DATE_SUCCESS : Code.GET_USER_INIT_DATE_FAILED, initDate);
    }

    @Override
    public Result getUser(String userCode, String userPasswd) {
        User user = userDao.getUser(userCode, userPasswd);
        if (user != null) {
            user.setCurrentWeek(Utils.calcCurrentWeek(new Date(), user.getTermInitialDate()));
            return new Result(Code.GET_USER_SUCCESS, user);
        }
        return new Result(Code.GET_USER_FAILED, null);
    }

}
