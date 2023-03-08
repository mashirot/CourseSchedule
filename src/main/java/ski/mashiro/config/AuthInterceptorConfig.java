package ski.mashiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ski.mashiro.interceptor.AuthInterceptor;

/**
 * @author MashiroT
 */
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor authInterceptorFactory() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorFactory())
                .addPathPatterns("/**");
    }
}
