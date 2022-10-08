package ski.mashiro.service;

import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;

/**
* @author MashiroT
*/
public interface CourseService {

    /**
     * 添加课程
     * @param course 课程对象
     * @param tableName 表名
     * @return 结果
     */
    Result saveCourse(Course course, String tableName);

    /**
     * 从文件获取课程列
     * @param multipartFile 文件
     * @param tableName 表名
     * @return 结果
     */
    Result saveCoursesFromFile(MultipartFile multipartFile, String tableName);

    /**
     * 删除课程
     * @param courseName 课程名
     * @param tableName 表名
     * @return 结果
     */
    Result deleteByCourseName(String courseName, String tableName);

    /**
     * 修改课程信息
     * @param course 新课程对象
     * @param tableName 表名
     * @return 结果
     */
    Result updateCourse(Course course, String tableName);

    /**
     * 通过课程名获取课程
     * @param courseName 课程名
     * @param tableName 表名
     * @return 返回课程对象
     */
    Result getCourseByCourseName(String courseName, String tableName);

    /**
     * 获取同一天的课程
     * @param courseDate 日期
     * @param tableName 表名
     * @return 结果集合
     */
    Result listAllByCourseDateCourses(String courseDate, String tableName);

    /**
     * 获取所有课程
     * @param tableName 表名
     * @return 结果集合
     */
    Result listAllCourses(String tableName);

    /**
     * 获取同一天的有效课程
     * @param courseDate 日期
     * @param initDate 开始日期
     * @param tableName 表名
     * @return 结果集合
     */
    Result listAllEffectiveByCourseDateCourses(String courseDate, String initDate, String tableName);

    /**
     * 获取所有有效课程
     * @param initDate 开始日期
     * @param tableName 表名
     * @return 结果集合
     */
    Result listAllEffectiveCourses(String initDate, String tableName);
}
