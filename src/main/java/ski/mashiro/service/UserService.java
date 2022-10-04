package ski.mashiro.service;

import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

import java.util.Date;

/**
 * @author MashiroT
 */
public interface UserService {

    /**
     * 添加用户
     * @param user 入参
     * @return 结果
     */
    Result saveUser(User user);

    /**
     * 删除用户
     * @param userCode 学号
     * @param userPasswd 密码
     * @return 结果
     */
    Result deleteUser(String userCode, String userPasswd);

    /**
     * 同步日期
     * @param userCode 学号
     * @param currentDate 当前日期
     * @return 结果
     */
    Result updateCurrentWeek(String userCode, Date currentDate);

    /**
     * 修改用户密码
     * @param userCode 学号
     * @param userPassword 密码
     * @return 结果
     */
    Result updateUserPassword(String userCode, String userPassword);

    /**
     * 判断用户是否存在
     * @param userCode 学号
     * @param userPasswd 密码
     * @return 结果
     */
    Result getUser(String userCode, String userPasswd);
}
