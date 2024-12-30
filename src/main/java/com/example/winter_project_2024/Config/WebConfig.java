package com.example.winter_project_2024.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] ALLOW_ORIGINS = {
            "http://118.218.179.22:9998",
            "http://192.168.45.85:80"
    };

    private static final String[] ALLOW_METHODS = {
            "GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALLOW_ORIGINS) // 허용할 출처
                .allowedMethods(ALLOW_METHODS) // 허용할 HTTP method
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키 인증 요청 허용을 위해선 true로 설정해줘야합니다.
    }
}