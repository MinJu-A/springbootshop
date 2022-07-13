package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
//SpringSecurityFilterChain 자동으로 포함하게 해주는 Annotation
@EnableWebSecurity
//WebSecurityConfigurerAdapter 지원이 되지 않기 때문에 아래의 방법으로 해결한다
public class SecurityConfig    {

    @Autowired
    MemberService memberService;

    @Bean
//    원래는 별개지만 지금 체이닝을 걸어놓은것.
//    http 요청에 대한 보안 설정. 페이지 권한, 로그인 페이지, 로그아웃 메소드 설정 예정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")//로그인 페이지 url
                .defaultSuccessUrl("/")//로그인 성공 시 이동할 url 설정
                .usernameParameter("email")//로그인 시 사용할 파라미터 이름으로 email.지정
                .failureUrl("/members/login/error") //로그인 실패 시 이동 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))//로그아웃 url
                .logoutSuccessUrl("/")//로그아웃 성공 시 이동할 url
         ;

//        permitAll() 모든 사용자가 로그인 없이 경로에 접근 가능
//        /admin 은 ADMIN Role만 접근 가능
        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;


        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //인증되지 않은 사용자 접근 시 수행
        ;


        return http.build();
    }

    @Bean
//    해시 함수를 이용하여 비밀번호를 암호화하여 져장
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
