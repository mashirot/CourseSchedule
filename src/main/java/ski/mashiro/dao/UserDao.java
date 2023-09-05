package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ski.mashiro.entity.User;

/**
 * @author MashiroT
 */
@Mapper
public interface UserDao {
    int saveUser(@Param("user") User user);
    String getPasswordByUsername(@Param("username") String username);
    User getUserByUsername(@Param("username") String username);
    User getUserByApiToken(@Param("username") String username, @Param("apiToken") String apiToken);
    String getApiTokenByUsername(@Param("username") String username);
    int updateUser(@Param("user") User user);
}
