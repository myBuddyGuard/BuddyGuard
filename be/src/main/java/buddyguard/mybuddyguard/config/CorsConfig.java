package buddyguard.mybuddyguard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://buddyguard.site:5173",
                        "http://localhost:5173",
                        "https://buddyguard.site",
                        "http://192.168.19.1:5173",
                        "http://172.16.1.38:5173",
                        "http://192.168.55.244:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH") // 허용할 HTTP 메서드 설정
                .allowedHeaders("Authorization", "Content-Type") // 허용할 헤더 설정
                .allowCredentials(true); // 인증 정보 허용
    }
}
