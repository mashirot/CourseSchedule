package ski.mashiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ski.mashiro.interceptor.AuthInterceptor;

/**
 * @author MashiroT
 */
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthInterceptorConfig(ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        this.objectMapper = objectMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public AuthInterceptor authInterceptorFactory() {
        return new AuthInterceptor(objectMapper, stringRedisTemplate);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorFactory())
                .addPathPatterns("/**");
    }
}
