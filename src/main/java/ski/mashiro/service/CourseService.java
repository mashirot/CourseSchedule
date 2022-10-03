package ski.mashiro.service;

import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;

/**
* @author MashiroT
*/
public interface CourseService {
    /**
     * 添加课程
     * @param course 课程对象
     * @return 结果
     */
    Result saveCourse(Course course);

    /**
     * 删除课程
     * @param courseName 课程名
     * @return 结果
     */
    Result deleteByCourseName(String courseName);

    /**
     * 修改课程信息
     * @param course 新课程对象
     * @return 结果
     */
    Result updateCourse(Course course);

    /**
     * 通过课程名获取课程
     * @param courseName 课程名
     * @return 返回课程对象
     */
    Result getCourseByCourseName(String courseName);

    /**
     * 获取同一天的课程
     * @param courseDate 日期
     * @return 结果集合
     */
    Result listAllByCourseDateCourses(String courseDate);

    /**
     * 获取所有课程
     * @return 结果集合
     */
    Result listAllCourses();

    /**
     * 获取同一天的有效课程
     * @param courseDate 日期
     * @return 结果集合
     */
    Result listAllEffectiveByCourseDateCourses(String courseDate);

    /**
     * 获取所有有效课程
     * @return 结果集合
     */
    Result listAllEffectiveCourses();
}
