package ski.mashiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ski.mashiro.interceptor.LoginInterceptor;

/**
 * @author MashiroT
 */
@SpringBootApplication
public class ClassScheduleApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ClassScheduleApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor());
    }
}
