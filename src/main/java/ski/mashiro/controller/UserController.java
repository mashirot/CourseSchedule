package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.JwtUtils;
import ski.mashiro.dto.Result;
import ski.mashiro.vo.UserInfoVo;
import ski.mashiro.vo.UserLoginVo;
import ski.mashiro.vo.UserRegVo;

import javax.servlet.http.HttpServletRequest;

import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public Result<UserLoginVo> login(HttpServletRequest request, @RequestBody UserLoginVo userLogin) {
        Result<User> rs = userService.getUserByPassword(userLogin);
        if (rs.code() != USER_LOGIN_SUCCESS) {
            return Result.failed(rs.code(), rs.msg());
        }
        String authToken = JwtUtils.createToken(rs.data().getUid(), rs.data().getUsername(), rs.data().getTermStartDate());
        return Result.success(USER_LOGIN_SUCCESS, new UserLoginVo(userLogin.getUsername(), authToken));
    }

//    jwt无状态，只能自动过期，本项目未使用Redis
//    @TokenRequired
//    @PostMapping("/logout")
//    public Result<String> logout(HttpServletRequest req) {
//        req.getSession().removeAttribute("Authorization");
//        return Result.success(USER_LOGOUT_SUCCESS, null);
//    }

    @TokenRequired
    @GetMapping("/info")
    public Result<UserInfoVo> getUserInfo(HttpServletRequest request) {
        var username = (String) request.getSession().getAttribute("username");
        return userService.getUserInfoByUsername(username);
    }

    @TokenRequired
    @PostMapping("/modify")
    public Result<String> modifyUser(HttpServletRequest request, @RequestBody User user) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        user.setUid(uid);
        return userService.updateUser(user);
    }
}
