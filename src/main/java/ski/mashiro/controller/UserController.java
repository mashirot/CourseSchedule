package ski.mashiro.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.common.Result;
import ski.mashiro.entity.User;
import ski.mashiro.service.UserService;
import ski.mashiro.dto.UserInfoDTO;
import ski.mashiro.dto.UserLoginDTO;
import ski.mashiro.dto.UserRegDTO;

import javax.servlet.http.HttpServletRequest;

import static ski.mashiro.constant.RedisKeyConstant.*;
import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    public UserController(UserService userService, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("/register")
    public Result<String> register(HttpServletRequest request, @RequestBody UserRegDTO userReg) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        try {
            if (!captcha.equalsIgnoreCase(userReg.getCaptchaCode())) {
                return Result.failed(CAPTCHA_VERIFY_FAILED, "验证码错误");
            }
        } catch (Exception e) {
            return Result.failed(CAPTCHA_VERIFY_FAILED, "验证码错误");
        }
        return userService.saveUser(userReg);
    }

    @PostMapping("/login")
    public Result<UserLoginDTO> login(@RequestBody UserLoginDTO userLogin) {
        Result<UserLoginDTO> rs = userService.getUserByPassword(userLogin);
        if (rs.code() != USER_LOGIN_SUCCESS) {
            return Result.failed(rs.code(), rs.msg());
        }
        UserLoginDTO user = rs.data();
        return Result.success(USER_LOGIN_SUCCESS, user);
    }

    @TokenRequired
    @GetMapping ("/logout")
    public Result<String> logout(HttpServletRequest request) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        stringRedisTemplate.delete(USER_KEY + uid + USER_USERNAME);
        stringRedisTemplate.delete(USER_KEY + uid + USER_INFO);
        stringRedisTemplate.delete(USER_KEY + uid + USER_CURR_WEEK);
        request.getSession().removeAttribute("uid");
        return Result.success(USER_LOGOUT_SUCCESS, null);
    }

    @TokenRequired
    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo(HttpServletRequest request) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        return userService.getUserInfoByUsername(uid);
    }

    @TokenRequired
    @PostMapping("/modify")
    public Result<String> modifyUser(HttpServletRequest request, @RequestBody User user) {
        var uid = (Integer) request.getSession().getAttribute("uid");
        user.setUid(uid);
        return userService.updateUser(user);
    }
}
