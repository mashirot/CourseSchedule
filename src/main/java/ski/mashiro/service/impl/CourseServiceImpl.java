package ski.mashiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.extra.Weeks;
import ski.mashiro.dao.CourseDao;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.util.FileUtils;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.vo.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public Result<String> delCourse(CourseSearchDto courseSearchDto) {
        int rs = courseDao.delCourseByCondition(courseSearchDto);
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
    public Result<List<CourseDto>> listCourseByCondition(CourseSearchDto courseSearchDto) {
        if (courseSearchDto.getTermStartDate() != null) {
            courseSearchDto.setCurrWeek(Weeks.between(LocalDateTime.ofInstant(courseSearchDto.getTermStartDate().toInstant(), ZoneId.systemDefault()), LocalDateTime.now()).getAmount());
        }
        return listCourse(courseSearchDto);
    }

    @Override
    public Result<List<CourseDto>> listCourse(CourseSearchDto courseSearchDto) {
        List<Course> courses = courseDao.listCourseByCondition(courseSearchDto);
        if (courses == null) {
            return Result.failed(COURSE_LIST_FAILED, null);
        }
        List<CourseDto> courseDtoList = new ArrayList<>(courses.size());
        for (Course course : courses) {
            if (courseSearchDto.getTermStartDate() != null && course.getOddWeek() != 0) {
                if ((courseSearchDto.getCurrWeek() & 1) == (course.getOddWeek() & 1)) {
                    courseDtoList.add(new CourseDto(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                            course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getCredit()));
                }
                continue;
            }
            courseDtoList.add(new CourseDto(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                    course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getCredit()));
        }
        return Result.success(COURSE_LIST_SUCCESS, courseDtoList);
    }
}
