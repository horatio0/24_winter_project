package com.example.winter_project_2024.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {                      //계층 권한 설정
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_USER
                """);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()  //해당 요청은 인증 없이 가능
                .anyRequest().authenticated()); //나머지 요청은 인증 필요


        http.formLogin(auth -> auth
                .loginPage("https://indianpoker.mooo.com/login")    //기본 로그인 페이지 설정
                .defaultSuccessUrl("/")     //성공시 리다이렉트할 URL
                .usernameParameter("memberId")      //로그인 폼에서 ID 필드의 name 속성
                .passwordParameter("memberPassword")        //로그인 폼에서 비밀번호 필드의 name 속성
                .failureUrl("/login?error=true"));     //실패시 리다이렉트할 URL

        http.sessionManagement(session-> session
                .maximumSessions(1)     //사용자 한명이 가질 수 있는 최대 세션 개수 (다중 로그인 제한)
                .maxSessionsPreventsLogin(true));       //세션 개수 초과 시 추가 로그인 차단 = true

        http.sessionManagement(session -> session
                .sessionFixation().changeSessionId());      //세션 고정 공격을 막기 위한 코드

        http.logout(auth -> auth
                .logoutUrl("/logout")       //로그아웃 URL
                .logoutSuccessUrl("/"));        //로그아웃 성공 시 리다이렉트 할 URL

        //http.csrf(AbstractHttpConfigurer::disable);     //csrf 보호 (위변조 방지 어쩌구 저쩌구)비활성화

        return http.build();
    }
}