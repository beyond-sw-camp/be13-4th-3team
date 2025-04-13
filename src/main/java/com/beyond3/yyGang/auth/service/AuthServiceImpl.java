package com.beyond3.yyGang.auth.service;

import com.beyond3.yyGang.auth.dto.JwtToken;
import com.beyond3.yyGang.auth.JwtTokenProvider;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    // 로그인 로직
    @Override
    @Transactional
    public JwtToken signIn(String username, String password) {
        // 사용자 이메일, 비밀번호를 입력 받아서 인증 토큰 생성
        Optional<User> user = userRepository.findByEmail(username);

        if(user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            // 사용자가 없거나, 패스워드가 일치하지 않을 경우 예외 던지기
            throw new UserException(ExceptionMessage.INVALID_CREDENTIALS);
        }

        // 위의 과정을 통과할 경우 -> AccessToken + RefreshToken 발급
        return new JwtToken(
                jwtTokenProvider.createAccessToken(user.get().getEmail(), user.get().getRole()),
                jwtTokenProvider.createRefreshToken(user.get().getEmail())
        );
    }

    // 로그아웃 로직
    @Override
    public void logout(String token){
        // token에서 access Token만 추출
        String accessToken = jwtTokenProvider.resolveToken(token);

        // 추출한 토큰 유효성 확인
        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new UserException(ExceptionMessage.INVALID_ACCESS_TOKEN);
        };

        // access Token을 BlackList에 담고
        jwtTokenProvider.addBlackList(accessToken);

        // RefreshToken은 삭제하면 됨.
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }


    // 토큰 재발급 로직
    // 일반적으로 만료 되기 5분 정도 전에 물어보는 방식으로 가야할 것 같음
    @Override
    public JwtToken refresh(String token) {

        // refresh 토큰 추출
        String refreshToken = jwtTokenProvider.resolveToken(token);

        // 해당 토큰이 유효한지 확인 -> RefreshToken 이 이미 만료된 경우 재발급 불가
        if(refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)){
            throw new UserException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        };

        // 추출한 토큰에서 사용자 추출
        User user = userRepository.findByEmail(jwtTokenProvider.getUserName(refreshToken))
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // Refresh토큰의 유효성 검사
        if(!jwtTokenProvider.isValidRefreshToken(refreshToken)) {
            throw new UserException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }

        // Refresh는 이전에 쓰던거 그대로 유지
        return new JwtToken(
                jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole()),
                refreshToken
        );
    }


}
