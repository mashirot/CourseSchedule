package ski.mashiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.dao.CourseDao;
import ski.mashiro.vo.CourseVo;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.util.FileUtils;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.dto.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Result<String> saveCourse(Course course) {
        int rs = courseDao.insertCourse(course);
        if (rs == 1) {
            return Result.success(COURSE_INSERT_SUCCESS, null);
        }
        return Result.failed(COURSE_INSERT_FAILED, null);
    }

    @Transactional(rollbackFor = IOException.class)
    @Override
    public Result<String> saveCourseInFile(MultipartFile courseFile, int uid) {
        Queue<String> strings;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(courseFile.getInputStream()))) {
            strings = FileUtils.fileDeserialize(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(FILE_DESERIALIZE_FAILED, null);
        }
        try {
            while (!strings.isEmpty()) {
                Course course = FileUtils.courseStringDeserialize(strings.poll(), uid);
                Result<String> stringResult = this.saveCourse(course);
                if (stringResult.code() == COURSE_INSERT_FAILED) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.failed(FILE_DESERIALIZE_FAILED, null);
                }
            }
        } catch (IOException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.failed(FILE_DESERIALIZE_FAILED, null);
        }
        return Result.success(FILE_DESERIALIZE_SUCCESS, null);
    }

    @Override
    public Result<String> delCourse(CourseSearchVo courseSearchVo) {
        int rs = courseDao.delCourseByCondition(courseSearchVo);
        if (rs != 0) {
            return Result.success(COURSE_DELETE_SUCCESS, null);
        }
        return Result.failed(COURSE_DELETE_FAILED, null);
    }

    @Override
    public Result<String> updateCourse(Course course) {
        int rs = courseDao.updateCourse(course);
        if (rs != 0) {
            return Result.success(COURSE_UPDATE_SUCCESS, null);
        }
        return Result.failed(COURSE_UPDATE_FAILED, null);
    }

    @Override
    public Result<List<CourseVo>> listCourseByCondition(CourseSearchVo courseSearchVo) {
        if (courseSearchVo.getTermStartDate() != null) {
            courseSearchVo.setCurrWeek(WeekUtils.getCurrWeek(courseSearchVo.getTermStartDate()));
        }
        return listCourse(courseSearchVo);
    }

    @Override
    public Result<List<CourseVo>> listCourse(CourseSearchVo courseSearchVo) {
        List<Course> courses = courseDao.listCourseByCondition(courseSearchVo);
        if (courses == null) {
            return Result.failed(COURSE_LIST_FAILED, null);
        }
        List<CourseVo> courseVoList = new ArrayList<>(courses.size());
        for (Course course : courses) {
            if (courseSearchVo.getTermStartDate() != null && course.getOddWeek() != 0) {
                if ((courseSearchVo.getCurrWeek() & 1) == (course.getOddWeek() & 1)) {
                    courseVoList.add(new CourseVo(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                            course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getCredit()));
                }
                continue;
            }
            courseVoList.add(new CourseVo(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                    course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getCredit()));
        }
        return Result.success(COURSE_LIST_SUCCESS, courseVoList);
    }
}
