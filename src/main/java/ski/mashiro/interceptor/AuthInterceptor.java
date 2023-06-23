package ski.mashiro.interceptor;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.util.JwtUtils;
import ski.mashiro.dto.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author MashiroT
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String authToken = request.getHeader("Authorization");
        Method method = ((HandlerMethod) handler).getMethod();
        if (method.isAnnotationPresent(TokenRequired.class)) {
            TokenRequired tokenRequired = method.getAnnotation(TokenRequired.class);
            if (tokenRequired.required()) {
                if (authToken == null) {
                    response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.failed(StatusCodeConstants.AUTH_VERIFY_FAILED, null)));
                    return false;
                }
                try {
                    authToken = authToken.split(" ")[1];
                    DecodedJWT decodedJwt = JwtUtils.getVerifier(authToken);
                    Integer uid = decodedJwt.getClaim("uid").asInt();
                    String username = decodedJwt.getClaim("username").asString();
                    Date termStartDate = decodedJwt.getClaim("termStartDate").asDate();
                    request.getSession().setAttribute("uid", uid);
                    request.getSession().setAttribute("username", username);
                    request.getSession().setAttribute("termStartDate", termStartDate);
                } catch (Exception e) {
                    response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.failed(StatusCodeConstants.AUTH_VERIFY_FAILED, null)));
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
