package com.beyond3.yyGang.auth;

import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.user.domain.Role_name;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component  // 빈으로 등록할 예정
public class JwtTokenProvider {

    private final SecretKey key;  // JWT 서명에 사용될 비밀 키
    private final long ACCESS_TOKEN_EXP = 1000L * 60L * 60L * 24L;
    private final long REFRESH_TOKEN_EXP = 1000L * 60L * 60L * 24L;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    // JwtTokenProvider 생성자
    public JwtTokenProvider(
            @Value("${jwt.secret.key}") String secretKey,
            UserDetailsService userDetailsService,
            RedisTemplate<String, String> redisTemplate) {

        // Secret Key를 Base64로 디코딩 -> 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // HMAC-SHA 알고리즘을 사용할 수 있는 서명용 비밀 키로 변환, this.key에 저장
        // BASE64 디코딩을 통해 더 안전하게 처리
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.userDetailsService = userDetailsService;

        this.redisTemplate = redisTemplate;
    }

    // Access Token 생성 메소드
    public String createAccessToken(String username, Role_name role) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("username", username);
        claims.put("role", role);

        return createToken(claims, ACCESS_TOKEN_EXP);
    }

    public String createRefreshToken(String username) {
        Map<String , Object > claims = Map.of("username" , username);
        String refreshToken = createToken(claims, REFRESH_TOKEN_EXP);

        // 생성한 Refresh Token은 Redis 서버에 저장
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, REFRESH_TOKEN_EXP, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public String createToken(Map<String , Object > claims, long tokenExp) {
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)           // 사용자 권한을 "auths"라는 클레임으로 추가
                .id(Long.toString(System.nanoTime()))   // 현재 시간을 기준으로 유니크한 jti(JWT ID) 생성
                .issuedAt(new Date())   // 발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + tokenExp))  // Key 만료 기간 설정
                .signWith(key)
                .compact();
    }

    // 토큰 정보 검증 메서드
    public boolean validateToken(String token) {
        return !parseClaims(token).getExpiration().before(new Date());
    }

    // 서버에 전달한 토큰 추출 메소드
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰을 복호화해서 토큰에 들어있는 정보를 꺼내는 메서드
    // 꺼낸 정보를 바탕으로 SecurityHolder에 들어갈 Authentication 객체를 생성해줌
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름 정보 가져오기
       String username = getUserName(token);
       UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 클레임(Claim)을 추출하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parser()   // JWT Parser를 생성
                    .verifyWith(key) // 서명 검증에 사용할 비밀 키 설정
                    .build()
                    .parseSignedClaims(token) // JWT 토큰을 파싱해 claim 생성
                    .getPayload();     // claim 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims();   // 만료된 토큰일 경우 예외에서 claim을 반환
        }
    }

    public boolean hasRole(String token) {
        return parseClaims(token).get("role") != null;
    }

    public String getUserName(String token){
        return parseClaims(token).get("username").toString();
    }

    public boolean isBlackListed(String token){
        String key = "BlackList:" + parseClaims(token).getId();
        return redisTemplate.hasKey(key);
    }

    public void addBlackList(String token) {
//        long tokenCreatedTime = parseClaims(token).getExpiration().getTime();   // 이 방식은 이미 만료된 경우 제대로 작동하지 않을 수 있음.
//        long blackListExp = tokenCreatedTime - System.currentTimeMillis();

        long tokenCreatedTime = parseClaims(token).getIssuedAt().getTime();
        long blackListExp = (tokenCreatedTime + ACCESS_TOKEN_EXP) - System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                "BlackList:" + parseClaims(token).getId(),
                "true",
                blackListExp,
                TimeUnit.MILLISECONDS);
    }

    // refresh 토큰 삭제
    public void deleteRefreshToken(String token) {
        String username = getUserName(token);
        redisTemplate.delete("refreshToken:" + username);
    }

    public boolean isValidRefreshToken(String refreshToken) {

        // accessToken에서 사용자 이름 얻어오기
        String username = getUserName(refreshToken);

        // 사용자 이름을 바탕으로 Redis에서 RefreshToken 얻어오기
        String storedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + username);

        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);  // 얻어온 값이 null인지 아닌지를 반환
    }
}
