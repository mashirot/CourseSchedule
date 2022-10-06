package ski.mashiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author MashiroT
 */
@Mapper
public interface TableDao {

    int createTable(@Param("tableName") String tableName);
    int deleteTable(@Param("tableName") String tableName);
}
