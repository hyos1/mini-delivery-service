package com.myapp.delivery.global.config;

import com.myapp.delivery.global.security.JwtAuthenticationFilter;
import com.myapp.delivery.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)       // 토큰 방식이라 끔
                .formLogin(AbstractHttpConfigurer::disable)  // 기본 로그인 화면 끔
                .httpBasic(AbstractHttpConfigurer::disable)  // 기본 인증 팝업 끔
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 안 씀
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()  // 이 둘은 토큰 없이 통과
                        .requestMatchers(GET, "/api/menus", "/api/menus/*").permitAll()
                        .requestMatchers(POST,"/api/menus").hasRole("OWNER")
                        .anyRequest().authenticated()
                )       // 나머지는 인증된 사람만
                .addFilterBefore(new JwtAuthenticationFilter(objectMapper, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);   // 내가 만든 필터 끼우기

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}