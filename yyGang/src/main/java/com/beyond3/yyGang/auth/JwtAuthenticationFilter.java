package com.beyond3.yyGang.auth;

import com.beyond3.yyGang.auth.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWT Token을 필터링, 유효한 토큰인 경우 사용자 인증 정보를 SecurityContext에 저장
    // UsernamePasswordAuthenticationFilter 이전이 실행되는 내용

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 회원이 발급 받은 Token을 들고 Request 를 보낼 때
        // 받은 Request 의 Authorization 헤더에서 Token만 Parsing 해서 추출
        String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));


        if(token != null && jwtTokenProvider.validateToken(token)
                && jwtTokenProvider.hasRole(token)
                && !jwtTokenProvider.isBlackListed(token)) {
            // Token이 유효하면 Token에서 Authentication 정보를 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 얻은 authentication 객체를 SecurityContextHolder에 넣어줌
            // 해당 과정은 로그인 후 매 요청마다 실행되는 과정임
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

//        try {
//            if(token != null && jwtTokenProvider.validateToken(token)
//                    && jwtTokenProvider.hasRole(token)
//                    && !jwtTokenProvider.isBlackListed(token)) {
//                // Token이 유효하면 Token에서 Authentication 정보를 추출
//                Authentication authentication = jwtTokenProvider.getAuthentication(token);
//
//                // 얻은 authentication 객체를 SecurityContextHolder에 넣어줌
//                // 해당 과정은 로그인 후 매 요청마다 실행되는 과정임
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//            filterChain.doFilter(request, response);
//
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
//        }
    }
}
