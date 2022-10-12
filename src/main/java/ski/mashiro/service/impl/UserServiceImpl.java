package ski.mashiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ski.mashiro.dao.TableDao;
import ski.mashiro.dao.UserDao;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.Encrypt;
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
        user.setPasswordSalt(Encrypt.generateSalt(50));
        user.setUserPassword(Encrypt.encrypt(user.getUserPassword(), user.getPasswordSalt()));
        user.setUserApiToken(Encrypt.encrypt(Encrypt.generateSalt(50), Encrypt.generateSalt(50)));
        if (userDao.saveUser(user) == 1) {
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
        if (user.getUserPassword() != null) {
            user.setUserPassword(Encrypt.encrypt(user.getUserPassword(), userDao.getPasswordSalt(user.getUserCode())));
        }
        return new Result(userDao.updateUser(user) == 1 ? Code.UPDATE_USER_SUCCESS : Code.UPDATE_USER_FAILED, null);
    }

    @Override
    public Result getInitDate(String userCode) {
        Date initDate = userDao.getInitDate(userCode);
        return new Result(initDate != null ? Code.GET_USER_INIT_DATE_SUCCESS : Code.GET_USER_INIT_DATE_FAILED, initDate);
    }

    @Override
    public Result getUser(String userCode, String userPasswd) {
        String password = Encrypt.encrypt(userPasswd, userDao.getPasswordSalt(userCode));
        User user = userDao.getUser(userCode, password);
        if (user != null) {
            user.setCurrentWeek(Utils.calcCurrentWeek(new Date(), user.getTermInitialDate()));
            return new Result(Code.GET_USER_SUCCESS, user);
        }
        return new Result(Code.GET_USER_FAILED, null);
    }

    @Override
    public Result getUserByApiToken(User user) {
        User rsUser = userDao.getUserByApiToken(user);
        return new Result(rsUser != null ? Code.API_LOGIN_SUCCESS : Code.API_LOGIN_FAILED, rsUser);
    }

    @Override
    public Result getUserApiToken(String userCode) {
        String apiToken = userDao.getUserApiToken(userCode);
        return new Result(apiToken != null ? Code.API_GET_SUCCESS : Code.API_GET_FAILED, apiToken);
    }

}
