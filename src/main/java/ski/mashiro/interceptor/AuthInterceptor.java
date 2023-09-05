package ski.mashiro.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ski.mashiro.annotation.TokenRequired;
import ski.mashiro.constant.RedisKeyConstant;
import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.common.Result;
import ski.mashiro.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author MashiroT
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthInterceptor(ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        this.objectMapper = objectMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }


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
                    return authFailed(response);
                }
                try {
                    authToken = authToken.split(" ")[1];
                    DecodedJWT decodedJwt = JwtUtils.getVerifier(authToken);
                    int uid = decodedJwt.getClaim("uid").asInt();
                    if (stringRedisTemplate.opsForValue().get(RedisKeyConstant.USER_KEY + uid + RedisKeyConstant.USER_INFO) == null) {
                        return authFailed(response);
                    }
                    request.getSession().setAttribute("uid", uid);
                } catch (Exception e) {
                    return authFailed(response);
                }
            }
        }
        return true;
    }

    private boolean authFailed(HttpServletResponse response) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(Result.failed(StatusCodeConstants.AUTH_VERIFY_FAILED, null)));
        return false;
    }
}
