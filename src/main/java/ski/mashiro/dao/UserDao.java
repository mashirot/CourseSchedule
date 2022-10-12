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
    int deleteUser(@Param("userCode") String userCode);
    int updateUser(@Param("user") User user);
    Date getInitDate(@Param("userCode") String userCode);
    String getPasswordSalt(@Param("userCode") String userCode);
    User getUser(@Param("userCode") String userCode, @Param("userPassword") String userPassword);
    User getUserByApiToken(@Param("user") User user);
    String getUserApiToken(@Param("userCode") String userCode);
}
