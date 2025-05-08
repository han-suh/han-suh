package com.artu.fullstack_team_project_application.config;


import com.artu.fullstack_team_project_application.jwt.JwtLoginFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 커스텀 설정이라고 선언
public class SecurityConfig {

    private final JwtLoginFilter jwtLoginFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // SecurityFilterChain는 보안 설정을 커스터마이징하려면 구현해야 하는 필수 메서드임.

        return http
                .cors(Customizer.withDefaults()) //cors 허용
                .csrf(csrf -> csrf.disable()) // 서버가 요청을 받을 때 CSRF 토큰 검사를 하지 않도록 설정.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/",
                                        "/vite.svg",
                                        "index.html",
                                        "/assets/**",
                                        "/favicon.ico",
                                        "/event/**",
                                        "/api/events/**",
                                        "/api/widgets/**",
                                        "/user/jwt/check.do",
                                        "/user/jwt/login.do"
//                                        "/user/oauth/login.do",
//                                        "/user/oauth/signup.do"
                                ).permitAll()
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                                .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        // 더이상 세션 기반의 인증을 사용하지 않겠다. -> jwt 기반 인증을 생성해서 추가해야함
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    // 비밀번호를 암호화해서 비교하기 위해 어떤 인코딩 방식을 쓸지 명시함.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    // authenticationManager()는 스프링이 내부적으로 구성한 빌더를 사용하는 편리한 방식일 뿐임.
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
        //HttpSecurity 에서 AuthenticationManager "빌더"를 꺼내서 빌딩하고 반환하는 것일 뿐.
    }

}
