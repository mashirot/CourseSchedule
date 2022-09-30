package ski.mashiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import generator.ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.dao.CourseDao;
import org.springframework.stereotype.Service;

/**
* @author MashiroT
* @description 针对表【courses】的数据库操作Service实现
* @createDate 2022-09-30 14:55:19
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course>
implements CourseService {

}
