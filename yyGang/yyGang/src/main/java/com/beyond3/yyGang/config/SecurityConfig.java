package com.beyond3.yyGang.config;

import com.beyond3.yyGang.auth.JwtAuthenticationFilter;
import com.beyond3.yyGang.auth.JwtTokenProvider;
import com.beyond3.yyGang.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableJpaAuditing
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean   // 빈 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authService) throws Exception {

        // 스프링 시큐리티가 HTTP 요청에 대한 보안 설정을 적용
        http
                .csrf(AbstractHttpConfigurer::disable)      // JWT를 사용하니 세션 사용 비활성화
                .cors(corsCustomizer ->
                        corsCustomizer.configurationSource(getCorsConfigurationSource())
                )
                .httpBasic(AbstractHttpConfigurer::disable)     // 기본 HTTP 인증 방식 Basic Authentication 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sesstionManagement) ->
                        sesstionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // 세션 관리 정책 -> 서버가 클라이언트의 세션 상태를 유지하지 않도록 -> 매 요청마다 인증 정보를 제공하도록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, authService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(
                        exception ->
                                exception
                                        .authenticationEntryPoint(
                                        (request, response, authException) ->{
                                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                            response.setContentType("application/json");
                                            response.setCharacterEncoding("UTF-8");
                                            response.getWriter().write("{\"error\": \"인증이 필요합니다.\"}");
                                        })
                                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                                            response.setStatus(HttpStatus.FORBIDDEN.value());})
                )
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                                .requestMatchers("/s3/*").permitAll()
                                .requestMatchers("/user/email/exists").permitAll()
                                .requestMatchers("/user/join").permitAll() // 로그인 하지 않은 사용자 회원가입 페이지 접근 가능
                                .requestMatchers("/user/login").permitAll() // 로그인 하지 않는 사용자 login 페이지 접근 가능
                                .anyRequest().authenticated());  // 위에 명시된 경로 외의 다른 모든 요청은 인증된 사용자만 접근할 수 있도록 설정
                // JWT Token 인증 방식의 미리 만들어둔 Filter를 Filter Chain에 추가

        return http.build();
    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // BCryptPasswordEncoder를 기본으로 다양한 여러 암호화 방식을 추가할 수 있음
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // CORS 정책을 설정하는 코드 -> 웹 애플리케이션이 다른 도메인, 프로토콜 또는 포트에서 호스팅되는 리소스에 접근하려 할 때
    // 발생하는 보안 문제를 해결하기 위한 표준
    private static CorsConfigurationSource getCorsConfigurationSource() {
        return (request) -> {
            // CorsConfiguration 객체를 생성해서 CORS 정책을 설정한다.
            CorsConfiguration configuration = new CorsConfiguration();

            // CORS 요청에서 허용할 출처를 설정한다.
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
            // configuration.setAllowedOriginPatterns(List.of("*"));

            // CORS 요청에서 허용할 HTTP 메소드를 지정한다.
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

            // 클라이언트가 요청 시 사용할 수 있는 헤더를 지정한다.
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

            // 클라이언트가 응답에서 접근할 수 있는 헤더를 지정한다.
            configuration.setExposedHeaders(List.of("Authorization"));

            // 자격 증명(쿠키, 세션) 허용 여부를 설정한다.
            configuration.setAllowCredentials(true);

            // CORS Preflight 요청(OPTIONS 메서드)을 브라우저가 캐싱하는 시간(초 단위)을 설정한다.
            configuration.setMaxAge(3600L);

            return configuration;
        };
    }
}
