package ski.mashiro.service;

import ski.mashiro.pojo.Course;

import java.util.List;

/**
* @author MashiroT
*/
public interface CourseService {
    /**
     * 添加课程
     * @param course 课程对象
     * @return 结果
     */
    boolean saveCourse(Course course);

    /**
     * 删除课程
     * @param courseName 课程名
     * @return 结果
     */
    boolean deleteByCourseName(String courseName);

    /**
     * 修改课程信息
     * @param course 新课程对象
     * @return 结果
     */
    boolean updateCourse(Course course);

    /**
     * 通过课程名获取课程
     * @param courseName 课程名
     * @return 返回课程对象
     */
    Course getCourseByCourseName(String courseName);

    /**
     * 获取同一天的课程
     * @param courseDate 日期
     * @return 结果集合
     */
    List<Course> listAllByCourseDateCourses(String courseDate);

    /**
     * 获取所有课程
     * @return 结果集合
     */
    List<Course> listAllCourses();
}
