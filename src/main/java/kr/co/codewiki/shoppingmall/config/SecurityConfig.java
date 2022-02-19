package kr.co.codewiki.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션
@EnableWebSecurity // 보안 설정을 커스터마이징 할 수 있는 애노테이션. SpringSecurityFilterChane 이 자동으로 포함됨
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter 를 상속받아서 보안 설정

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // http 요청에 대한 보안 설정_ 페이지 설정 (권한, 로그인 페이지, 로그아웃 메소드 등등)
    }

    @Bean // BCryptPasswordEncoder 를 Bean 으로 등록해서 사용
    public PasswordEncoder passwordEncoder(){
        // 해킹 시 회원 정보 노출 방지 (비번같은거 털릴 때 대비)_ BCryptPasswordEncoder 의 해시 함수를 이용해서 비번 암호화
        //
        return new BCryptPasswordEncoder();
    }
}
