package ski.mashiro.service;

import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

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
     * @return 结果
     */
    Result deleteUser(String userCode);

    /**
     * 更新用户信息
     * @param user 入参
     * @return 结果
     */
    Result updateUser(User user);

    /**
     * 获取学期开始日期
     * @param userCode 学号
     * @return 结果
     */
    Result getInitDate(String userCode);

    /**
     * 判断用户是否存在
     * @param userCode 学号
     * @param userPasswd 密码
     * @return 结果
     */
    Result getUser(String userCode, String userPasswd);
}
