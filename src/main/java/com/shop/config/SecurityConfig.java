package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//SpringSecurityFilterChain 자동으로 포함하게 해주는 Annotation
@EnableWebSecurity
//WebSecurityConfigurerAdapter을 상속 받아 보안 설정 커스터마이징이 가능하다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
//    http 요청에 대한 보안 설정. 페이지 권한, 로그인 페이지, 로그아웃 메소드 설정 예정
    protected void configure(HttpSecurity http) throws Exception{

    }

    @Bean
//    해시 함수를 이용하여 비밀번호를 암호화하여 져장
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
