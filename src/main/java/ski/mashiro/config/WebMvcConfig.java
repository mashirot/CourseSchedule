package ski.mashiro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author MashiroT
 */
@Configuration
public class WebMvcConfig {

    @Bean
    @ConfigurationProperties("cors.configuration")
    public CorsConfiguration corsConfiguration() {
        return new CorsConfiguration();
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建 CORS 配置对象
        CorsConfiguration config = corsConfiguration();
        // 2. 添加地址映射
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        // 3. 返回 CorsFilter 对象
        return new CorsFilter(corsConfigurationSource);
    }
}
