package ski.mashiro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import ski.mashiro.bo.CourseSearchBo;
import ski.mashiro.dao.CourseDao;
import ski.mashiro.dto.Result;
import ski.mashiro.pojo.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.util.FileUtils;
import ski.mashiro.util.JsonUtils;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.vo.CourseSearchVo;
import ski.mashiro.vo.CourseVo;
import ski.mashiro.vo.UserInfoVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static ski.mashiro.constant.StatusCodeConstants.*;
import static ski.mashiro.constant.RedisKeyConstant.*;

/**
 * @author MashiroT
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public CourseServiceImpl(CourseDao courseDao, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.courseDao = courseDao;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Result<String> saveCourse(Course course) {
        return courseDao.insertCourse(course) > 0 ? Result.success(COURSE_INSERT_SUCCESS, null) : Result.failed(COURSE_INSERT_FAILED, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> saveCourseInFile(MultipartFile courseFile, int uid) {
        if (courseFile.getOriginalFilename() == null || !"txt".equals(courseFile.getOriginalFilename().split("\\.")[1]) || courseFile.getSize() > 16 * 1024) {
            return Result.failed(FILE_DESERIALIZE_FAILED, null);
        }
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
        } catch (Exception e) {
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

//    Api用
    @Override
    public Result<List<CourseVo>> listCourseByCondition(CourseSearchVo courseSearchVo) {
        int currWeek = WeekUtils.getCurrWeek(courseSearchVo.getTermStartDate());
        var courseSearchBo = new CourseSearchBo(courseSearchVo.getUid(), courseSearchVo.getName(), courseSearchVo.getPlace(), courseSearchVo.getIsEffective() ? currWeek : null, courseSearchVo.getDayOfWeek(), courseSearchVo.getCredit(), courseSearchVo.getOddWeek());
        return listCourse(courseSearchBo);
    }

    @Override
    public Result<List<CourseVo>> listCourseByCondition(String username, CourseSearchVo courseSearchVo) {
        String key = USER_KEY + username;
        Integer uid = getUid(username);
        UserInfoVo userInfo;
        try {
            userInfo = objectMapper.readValue(stringRedisTemplate.opsForValue().get(key + USER_INFO), UserInfoVo.class);
        } catch (JsonProcessingException e) {
            log.warn("用户 {} CacheInfo 序列化失败：{}", username, e.getMessage());
            return null;
        }
        Integer currWeek;
        String currWeekJson;
        if ((currWeekJson = stringRedisTemplate.opsForValue().get(key + USER_CURR_WEEK)) != null) {
            currWeek = Integer.parseInt(currWeekJson);
        } else {
            currWeek = WeekUtils.getCurrWeek(userInfo.getTermStartDate());
            stringRedisTemplate.opsForValue().set(key + USER_CURR_WEEK, String.valueOf(currWeek), 30, TimeUnit.MINUTES);
        }
        var courseSearchBo = new CourseSearchBo(uid, courseSearchVo.getName(), courseSearchVo.getPlace(), courseSearchVo.getIsEffective() ? currWeek : null, courseSearchVo.getDayOfWeek(), courseSearchVo.getCredit(), courseSearchVo.getOddWeek());
        return listCourse(courseSearchBo);
    }

    @Override
    public Result<List<CourseVo>> listCourse(CourseSearchBo courseSearchBo) {
//        判断有无缓存
        String allCourseJson;
        String allCourseKey = COURSE_KEY + courseSearchBo.getUid() + COURSE_ALL;
        if (courseSearchBo.getCurrWeek() == null && (allCourseJson = stringRedisTemplate.opsForValue().get(allCourseKey)) != null) {
            try {
                return Result.success(COURSE_LIST_SUCCESS, JsonUtils.trans2List(objectMapper, allCourseJson, CourseVo.class));
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage());
            }
        }
        List<Course> courses = courseDao.listCourseByCondition(courseSearchBo);
        if (courses == null) {
            return Result.failed(COURSE_LIST_FAILED, null);
        }
        List<CourseVo> courseVoList = new ArrayList<>(courses.size());
        for (Course course : courses) {
//            查询有效 && 课程单双
            if (courseSearchBo.getCurrWeek() != null && course.getOddWeek() != 0) {
//                判断单双
                if ((courseSearchBo.getCurrWeek() & 1) == (course.getOddWeek() & 1)) {
                    courseVoList.add(new CourseVo(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                            course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getOddWeek() == 1 ? "单" : "双", course.getCredit()));
                }
                continue;
            }
//            查询全部 || 课程非单双
            courseVoList.add(new CourseVo(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                    course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getOddWeek() == 0 ? "-" : course.getOddWeek() == 1 ? "单" : "双", course.getCredit()));
        }
//        查询所有，加缓存
        if (courseSearchBo.getCurrWeek() == null) {
            try {
                stringRedisTemplate.opsForValue().set(allCourseKey, objectMapper.writeValueAsString(courseVoList), 24, TimeUnit.HOURS);
            } catch (JsonProcessingException e) {
                log.warn("{}", e.getMessage());
            }
        }
        return Result.success(COURSE_LIST_SUCCESS, courseVoList);
    }

    private Integer getUid(String username) {
        return Integer.parseInt(stringRedisTemplate.opsForValue().get(USER_KEY + username + USER_UID));
    }
}
