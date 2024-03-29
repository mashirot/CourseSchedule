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
import ski.mashiro.common.Result;
import ski.mashiro.entity.Course;
import ski.mashiro.service.CourseService;
import ski.mashiro.util.FileUtils;
import ski.mashiro.util.JsonUtils;
import ski.mashiro.util.WeekUtils;
import ski.mashiro.dto.CourseSearchDTO;
import ski.mashiro.dto.CourseDTO;
import ski.mashiro.dto.UserInfoDTO;

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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return Result.failed(FILE_DESERIALIZE_FAILED, null);
        }
        return Result.success(FILE_DESERIALIZE_SUCCESS, null);
    }

    @Override
    public Result<String> delCourse(CourseSearchDTO courseSearchDTO) {
        int rs = courseDao.delCourseByCondition(courseSearchDTO);
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
    public Result<List<CourseDTO>> listCourseByCondition(CourseSearchDTO courseSearchDTO) {
        String key = USER_KEY + courseSearchDTO.getUid();
        UserInfoDTO userInfo;
        try {
            userInfo = objectMapper.readValue(stringRedisTemplate.opsForValue().get(key + USER_INFO), UserInfoDTO.class);
        } catch (Exception e) {
            log.warn("用户Uid: {} CacheInfo 序列化失败: {}", courseSearchDTO.getUid(), e.getMessage());
            return null;
        }
        Integer currWeek;
        String currWeekStr;
        if ((currWeekStr = stringRedisTemplate.opsForValue().get(key + USER_CURR_WEEK)) != null) {
            currWeek = Integer.parseInt(currWeekStr);
        } else {
            currWeek = WeekUtils.getCurrWeek(userInfo.getTermStartDate());
            stringRedisTemplate.opsForValue().set(key + USER_CURR_WEEK, String.valueOf(currWeek), 30, TimeUnit.MINUTES);
        }
        var courseSearchBo = new CourseSearchBo(courseSearchDTO.getUid(), courseSearchDTO.getName(),
                courseSearchDTO.getPlace(), courseSearchDTO.getIsEffective() ? currWeek : null,
                courseSearchDTO.getDayOfWeek(), courseSearchDTO.getCredit(), courseSearchDTO.getOddWeek()
        );
        return listCourse(courseSearchBo);
    }

    @Override
    public Result<List<CourseDTO>> listCourseByCondition4Api(CourseSearchDTO courseSearchDTO) {
        CourseSearchBo searchBo = new CourseSearchBo();
        searchBo.setUid(courseSearchDTO.getUid());
        searchBo.setCurrWeek(courseSearchDTO.getIsEffective() ? WeekUtils.getCurrWeek(courseSearchDTO.getTermStartDate()) : null);
        searchBo.setDayOfWeek(courseSearchDTO.getDayOfWeek());
        return listCourse(searchBo);
    }

    @Override
    public Result<List<CourseDTO>> listCourse(CourseSearchBo courseSearchBo) {
//        判断有无缓存
        String allCourseJson;
        String allCourseKey = COURSE_KEY + courseSearchBo.getUid() + COURSE_ALL;
        if (courseSearchBo.getCurrWeek() == null && (allCourseJson = stringRedisTemplate.opsForValue().get(allCourseKey)) != null) {
            try {
                return Result.success(COURSE_LIST_SUCCESS, JsonUtils.trans2List(objectMapper, allCourseJson, CourseDTO.class));
            } catch (JsonProcessingException e) {
                log.warn(e.getMessage());
            }
        }
        List<Course> courses = courseDao.listCourseByCondition(courseSearchBo);
        if (courses == null) {
            return Result.failed(COURSE_LIST_FAILED, null);
        }
        List<CourseDTO> courseDTOList = new ArrayList<>(courses.size());
        for (Course course : courses) {
//            查询有效 && 课程单双
            if (courseSearchBo.getCurrWeek() != null && course.getOddWeek() != 0) {
//                判断单双
                if ((courseSearchBo.getCurrWeek() & 1) == (course.getOddWeek() & 1)) {
                    courseDTOList.add(new CourseDTO(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                            course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getOddWeek() == 1 ? "单" : "双", course.getCredit()));
                }
                continue;
            }
//            查询全部 || 课程非单双
            courseDTOList.add(new CourseDTO(course.getCourseId(), course.getDayOfWeek(), course.getStartTime() + " - " + course.getEndTime(),
                    course.getName(), course.getPlace(), course.getTeacher(), course.getStartWeek() + " - " + course.getEndWeek(), course.getOddWeek() == 0 ? "-" : course.getOddWeek() == 1 ? "单" : "双", course.getCredit()));
        }
//        查询所有，加缓存
        if (courseSearchBo.getCurrWeek() == null) {
            try {
                stringRedisTemplate.opsForValue().set(allCourseKey, objectMapper.writeValueAsString(courseDTOList), 24, TimeUnit.HOURS);
            } catch (JsonProcessingException e) {
                log.warn("{}", e.getMessage());
            }
        }
        return Result.success(COURSE_LIST_SUCCESS, courseDTOList);
    }
}
