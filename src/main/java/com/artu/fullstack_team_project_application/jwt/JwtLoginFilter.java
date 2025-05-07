package com.artu.fullstack_team_project_application.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 모든 요청을 가로채서 쿠키로 넘어온 jwt로 로그인 인증을 하는 곳
@Component
@AllArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        /* ㅡㅡㅡㅡㅡ  doFilterInternal에서 해야 할 작업의 순서
            1. 요청 헤더에서 JWT 추출
            2. JWT가 존재하고 유효한지 확인
            3. 유효하다면 인증 객체 생성 및 SecurityContext에 저장
            4. 필터 체인을 계속 진행
            (중간중간에). 예외 발생 시 적절한 에러 처리           */

        //요청 헤더에 Authorization 필드가 존재하는지, jwt가 있는지 검사
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // 따라서 헤더가 있고, 헤더 안에 정보중 Bearer로 시작하는 것이 있으면
            String token = authHeader.substring(7); // "Bearer "인 7글자를 빼고 나머지.

            boolean isValidToken = false;
            try {
                isValidToken = jwtUtil.validateToken(token); // 토큰 검증
            } catch (JwtException e) {
                logger.error(e.getMessage());
            }

            // 3. 유효하다면 인증 객체 생성 및 SecurityContext에 저장
            if (isValidToken) { // 여기까지 오면, 이 헤더가 로그인한 유저를 저장했다는 검증 마치고 유효한 token이므로,
                String username = jwtUtil.getUsername(token); // 유저 아이디 꺼내기
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // userDetails DTO로 받아서
                if (userDetails != null) { // 인증객체 만들어서
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken); // securityContext에 저장 끝
                    // 여기까지 하면 config에서 먹힘
                    System.out.println("ㅏㅏㅏㅏㅏㅏㅏ$%^*$*#^$%#*&(&*(*$%^$%^$%$#: JwtLoginFilter extends OncePerRequestFilter은 매번 실행되지 ");

                }
            }
        }
        // 4. 필터 체인을 계속 진행
        // 다음 필터로 요청 넘기기 / doFilter가 없으면 여기서 멈춤.
        filterChain.doFilter(request, response);
    }
}
