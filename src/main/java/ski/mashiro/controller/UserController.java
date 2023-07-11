package ski.mashiro.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.dto.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.vo.UserInfoVo;
import ski.mashiro.vo.UserLoginVo;
import ski.mashiro.vo.UserRegVo;

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
    public Result<String> register(HttpServletRequest request, @RequestBody UserRegVo userReg) {
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
    public Result<UserLoginVo> login(@RequestBody UserLoginVo userLogin) {
        Result<UserLoginVo> rs = userService.getUserByPassword(userLogin);
        if (rs.code() != USER_LOGIN_SUCCESS) {
            return Result.failed(rs.code(), rs.msg());
        }
        UserLoginVo user = rs.data();
        return Result.success(USER_LOGIN_SUCCESS, user);
    }

    @TokenRequired
    @GetMapping ("/logout")
    public Result<String> logout(HttpServletRequest request) {
        var username = (String) request.getSession().getAttribute("username");
        stringRedisTemplate.delete(USER_KEY + username + USER_UID);
        stringRedisTemplate.delete(USER_KEY + username + USER_INFO);
        stringRedisTemplate.delete(USER_KEY + username + USER_CURR_WEEK);
        request.getSession().removeAttribute("username");
        return Result.success(USER_LOGOUT_SUCCESS, null);
    }

    @TokenRequired
    @GetMapping("/info")
    public Result<UserInfoVo> getUserInfo(HttpServletRequest request) {
        var username = (String) request.getSession().getAttribute("username");
        return userService.getUserInfoByUsername(username);
    }

    @TokenRequired
    @PostMapping("/modify")
    public Result<String> modifyUser(HttpServletRequest request, @RequestBody User user) {
        var username = (String) request.getSession().getAttribute("username");
        user.setUsername(username);
        return userService.updateUser(user);
    }
}
