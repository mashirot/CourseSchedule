package ski.mashiro.service.impl;

import org.springframework.stereotype.Service;
import ski.mashiro.common.Result;
import ski.mashiro.dto.ApiCourseSearchDTO;
import ski.mashiro.dto.CourseDTO;
import ski.mashiro.dto.CourseSearchDTO;
import ski.mashiro.entity.User;
import ski.mashiro.service.ApiService;
import ski.mashiro.service.CourseService;
import ski.mashiro.service.UserService;

import java.util.List;
import java.util.Objects;

import static ski.mashiro.constant.StatusCodeConstants.USER_LOGIN_FAILED;
import static ski.mashiro.constant.StatusCodeConstants.USER_LOGIN_SUCCESS;

/**
 * @author MashiroT
 */
@Service
public class ApiServiceImpl implements ApiService {

    private final UserService userService;
    private final CourseService courseService;

    public ApiServiceImpl(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public Result<List<CourseDTO>> verifyAndSel(ApiCourseSearchDTO apiCourseSearchDTO) {
        User user = new User();
        user.setUsername(apiCourseSearchDTO.getUsername());
        user.setApiToken(apiCourseSearchDTO.getApiToken());
        Result<User> userByApiToken = userService.getUserByApiToken(user);
        if (userByApiToken.code() == USER_LOGIN_FAILED) {
            return Result.failed(USER_LOGIN_FAILED, null);
        }
        if (Objects.isNull(apiCourseSearchDTO.getIsEffective())) {
            return Result.success(USER_LOGIN_SUCCESS, null);
        }
        user = userByApiToken.data();
        CourseSearchDTO searchDTO = new CourseSearchDTO(user.getUid(), apiCourseSearchDTO.getDayOfWeek(), apiCourseSearchDTO.getIsEffective());
        searchDTO.setTermStartDate(user.getTermStartDate());
        return courseService.listCourseByCondition4Api(searchDTO);
    }

}
