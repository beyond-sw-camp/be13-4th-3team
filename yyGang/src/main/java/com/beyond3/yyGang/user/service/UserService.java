package com.beyond3.yyGang.user.service;

import com.beyond3.yyGang.auth.dto.JwtToken;
import com.beyond3.yyGang.auth.service.AuthService;
import com.beyond3.yyGang.cart.domain.Cart;
import com.beyond3.yyGang.cart.repository.CartRepository;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.user.dto.PasswordModifyDto;
import com.beyond3.yyGang.user.dto.UserInfoDto;
import com.beyond3.yyGang.user.dto.UserJoinDTO;
import com.beyond3.yyGang.user.dto.UserModifyDto;
import com.beyond3.yyGang.user.repository.UserRepository;
import com.beyond3.yyGang.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // 생성자를 통한 의존성 주입
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final AuthService authService;

    // 회원 가입 로직
    @Transactional
    public void join(UserJoinDTO userJoinDTO) {

        // Member 중복 조회
        validateMember(userJoinDTO.getEmail());

        if (!userJoinDTO.passwordMathcing()) {
            // 비밀번호, 비밀번호 확인이 일치하는지?
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        // user 객체 생성 + 비밀번호 암호화
        User user = userJoinDTO.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 회원 가입 시 회원 당 Cart 가 자동으로 생성되도록
        Cart cart = Cart.createCart(user);
        userRepository.save(user);
        cartRepository.save(cart);
    }

    // 회원 정보 수정 로직
    @Transactional
    public void update(String userEmail, UserModifyDto userModifyDto) {

        // email을 받아 회원 확인
        User user = getUserByEmail(userEmail);
        log.info(user.toString());
        // 비밀번호 외의 정보를 변경할 수 있도록
        user.updateUserInfo(userModifyDto);
        log.info("successfully updated user");
        log.info(user.toString());
    }

    // 회원 탈퇴 로직
    @Transactional
    public void delete(String email, String password) {
        User user = verifyUser(email, password);

        // 장바구니 먼저 삭제 -> 이 로직 있어야 할까?
        Cart cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new UserException(ExceptionMessage.CART_NOT_FOUND));

        cartRepository.delete(cart);    // 장바구니 먼저 지우고
        userRepository.delete(user);    // 회원 지우기
    }

    // 비밀번호 변경 로직
    @Transactional
    public JwtToken modifyPassword(String email, PasswordModifyDto passwordModifyDto){

        // email로 사용자를 찾고, 입력한 옛날 비밀번호가 일치하는지 확인
        User passwordModifyUser = verifyUser(email, passwordModifyDto.getOldPassword());

        // 일치하는 경우, 사용자의 비밀번호를 encode 해서 새로운 비밀번호로 변경
        if(!passwordModifyDto.getNewPassword().equals(passwordModifyDto.getNewPasswordConfirm())){
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        passwordModifyUser.setPassword(passwordEncoder.encode(passwordModifyDto.getNewPassword()));

        // 새로운 토큰 등록
        return authService.signIn(passwordModifyUser.getEmail(), passwordModifyDto.getNewPassword());
    }

    @Transactional
    public List<UserInfoDto> getAllUser(){
//        return userRepository.findAll().stream()
//                .map(user -> user.toUserInfoDto())
//                .collect(Collectors.toList());
        return null;
    }

    // 회원 가입 시 이메일을 토대로 이미 존재하는 회원인지 확인
    public void validateMember(String email){

        // 가입하려는 email이 이미 존재하는 이메일인지 확인
        Optional<User> findUsers = userRepository.findByEmail(email);
        if(findUsers.isPresent()){
            throw new UserException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        }
    }

    // 사용자의 password와 매개변수로 받은 password가 일치하는지 확인
    private User verifyUser(String email, String password) {
        // email로 사용자 찾기
        User user = getUserByEmail(email);

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }
        return user;
    }

    // email로 사용자 찾기
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

}
