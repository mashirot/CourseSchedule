package ski.mashiro.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MashiroT
 */

public class LoginInterceptor implements HandlerInterceptor {
    private static final String USER_CODE = "userCode";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        Cookie[] cookies = request.getCookies();
        String userCode = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_CODE.equals(cookie.getName())) {
                    userCode = cookie.getValue();
                }
            }
        }
        String[] uris = {"/Login.html", "/Register.html", "/users/register", "/users/login"};
        boolean flag = true;
        for (String s : uris) {
            if (s.equals(uri)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (request.getSession().getAttribute(USER_CODE) != null && request.getSession().getAttribute(USER_CODE).equals(userCode)) {
                return true;
            }
            response.sendRedirect("/Login.html");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
