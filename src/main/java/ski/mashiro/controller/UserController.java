package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.JwtUtils;
import ski.mashiro.vo.Result;

import javax.servlet.http.HttpServletRequest;

import static ski.mashiro.constant.StatusCodeConstants.USER_LOGIN_SUCCESS;
import static ski.mashiro.constant.StatusCodeConstants.USER_LOGOUT_SUCCESS;

/**
 * @author MashiroT
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        Result<User> rsUser = userService.getUserByPassword(user);
        if (rsUser.code() != USER_LOGIN_SUCCESS) {
            return Result.failed(rsUser.code(), rsUser.msg());
        }
        rsUser.data().setAuthToken(JwtUtils.createToken(rsUser.data().getUid()));
        return rsUser;
    }
    @TokenRequired
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest req) {
        req.getSession().removeAttribute("uid");
        return Result.success(USER_LOGOUT_SUCCESS, null);
    }

    @TokenRequired
    @PostMapping("/api")
    public Result<User> getApiToken(@RequestBody User user) {
        return userService.getApiTokenByUsername(user);
    }

    @TokenRequired
    @PostMapping("/modify")
    public Result<String> modifyUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
