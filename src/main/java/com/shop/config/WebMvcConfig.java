package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//   applicatoin.properties에 설정한 uploadPath의 프로퍼티 값을 읽어온다.
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        url에 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어온다.
//        로컬 컴퓨터에 저장된 파일을 읽어올 root 경로를 설정한다
        registry.addResourceHandler("/images/**").addResourceLocations(uploadPath);
    }


}
