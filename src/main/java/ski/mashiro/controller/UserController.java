package ski.mashiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.service.UserService;
import ski.mashiro.util.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author MashiroT
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{userCode}")
    public Result deleteUser(@PathVariable String userCode, HttpServletRequest request, HttpServletResponse response) {
        Result result = userService.deleteUser(userCode);
        Integer code = result.getCode();
        if (code.equals(Code.DELETE_USER_SUCCESS)) {
            resetCookie(request, response);
        }
        return result;
    }

    @PutMapping
    public Result updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{userCode}")
    public Result getInitDate(@PathVariable String userCode) {
        return userService.getInitDate(userCode);
    }

    @PostMapping("/login")
    public Result getUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        Result result = userService.getUser(user.getUserCode(), user.getUserPassword());
        if (result.getData() != null) {
            User data = (User) result.getData();
            Cookie userCode = new Cookie("userCode", data.getUserCode());
            Cookie userNickname = new Cookie("userNickname", data.getUserNickname());
            Cookie initDate = new Cookie("initDate", Utils.transitionDateToStr(data.getTermInitialDate()));
            Cookie userTableName = new Cookie("userTableName", data.getUserTableName());
            addCookie(response, userCode, userNickname, initDate, userTableName);
            HttpSession session = request.getSession();
            session.setAttribute("userCode", data.getUserCode());
            Cookie sessionId = new Cookie("JSESSIONID", session.getId());
            sessionId.setPath("/");
            response.addCookie(sessionId);
        }
        return result;
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        resetCookie(request, response);
        return new Result(Code.USER_LOGOUT_SUCCESS, null);
    }

    private static void resetCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie userCodeCookie = new Cookie("userCode", null);
        Cookie userNickname = new Cookie("userNickname", null);
        Cookie initDate = new Cookie("initDate", null);
        Cookie userTableName = new Cookie("userTableName", null);
        userCodeCookie.setMaxAge(0);
        userNickname.setMaxAge(0);
        initDate.setMaxAge(0);
        userTableName.setMaxAge(0);
        addCookie(response, userCodeCookie, userNickname, initDate, userTableName);
        request.getSession().invalidate();
    }

    private static void addCookie(HttpServletResponse response, Cookie userCodeCookie, Cookie userNickname, Cookie initDate, Cookie userTableName) {
        userCodeCookie.setPath("/");
        userNickname.setPath("/");
        initDate.setPath("/");
        userTableName.setPath("/");
        response.addCookie(userCodeCookie);
        response.addCookie(userNickname);
        response.addCookie(initDate);
        response.addCookie(userTableName);
    }

}
