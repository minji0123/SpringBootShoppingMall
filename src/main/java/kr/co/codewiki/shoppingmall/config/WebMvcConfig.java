package kr.co.codewiki.shoppingmall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 아까 application.properties 에 적었던 uploadPath 여기다가 대입
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // uploadPath 경로들/~
                .addResourceLocations(uploadPath); // 로컬에 저장된 파일을 읽어 올 root 경로 설정
    }

}