package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ski.mashiro.pojo.User;

import java.util.Date;

/**
 * @author FeczIne
 */
@Mapper
public interface UserDao {
    int saveUser(@Param("user") User user);
    int deleteUser(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
    int updateCurrentDate(@Param("userCode") String userCode, @Param("currentDate") Date currentDate, @Param("currentWeek") String currentWeek);
    int updateUserPassword(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
    Date getInitDate(@Param("userCode") String userCode);
    User getUser(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
}
