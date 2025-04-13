package com.beyond3.yyGang.user.controller;

import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.pay.service.PersonalAccountService;
import com.beyond3.yyGang.pay.dto.PersonalAccountDto;
import com.beyond3.yyGang.auth.service.AuthService;
import com.beyond3.yyGang.auth.dto.JwtToken;
import com.beyond3.yyGang.auth.JwtTokenProvider;
import com.beyond3.yyGang.auth.dto.UserLoginDto;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.dto.DeleteDto;
import com.beyond3.yyGang.user.dto.PasswordModifyDto;
import com.beyond3.yyGang.user.dto.UserInfoDto;
import com.beyond3.yyGang.user.dto.UserJoinDTO;
import com.beyond3.yyGang.user.dto.UserModifyDto;
import com.beyond3.yyGang.user.repository.UserRepository;
import com.beyond3.yyGang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관리")
public class UserController {

    private final UserService userService;
    private final PersonalAccountService personalAccountService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    // 회원 가입 처리 -> Post 방식
    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "가입 정보를 JSON으로 받아서 회원을 등록한다.")
    // 회원 가입 완료되면 데이터 전송 없이 201 상태코드 + 메시지만 반환
    public ResponseEntity<Void> join(@RequestBody @Valid UserJoinDTO userJoinDTO) {

        userService.join(userJoinDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 회원 이름 가져오기
    @GetMapping("/user-name")
    public ResponseEntity<String> getUserName(Principal principal) {
        User byEmail = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
        return ResponseEntity.ok(byEmail.getName());
    }

    @GetMapping("/user-eamil/{userId}")
    public ResponseEntity<String> getUserEmail(@PathVariable Long userId) {
        User emailByUserId = userRepository.findByUserId(userId);
        return ResponseEntity.ok(emailByUserId.getEmail());
    }

    // JWT 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디와 패스워드를 JSON으로 받아서 로그인한다.")
    public ResponseEntity<JwtToken> signIn(
            @Valid @RequestBody UserLoginDto userLoginDto){

        JwtToken jwtToken = authService.signIn(userLoginDto.getEmail(), userLoginDto.getPassword());

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그 아웃", description = "AccessToken 정보를 바탕으로 로그아웃 한다.")
    public ResponseEntity<Void> logOut(
            @RequestHeader("Authorization") String token){
        authService.logout(token);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "AccessToken을 Refresh Token 정보를 바탕으로 재발급한다.")
    public ResponseEntity<JwtToken> refresh(
            @RequestHeader("Authorization") String token
    ){
        JwtToken refreshToken = authService.refresh(token);
        return ResponseEntity.ok(refreshToken);
    }

//    @PostMapping("/test")
//    public String test1() {
//        return "success";
//    }

    // 회원 정보 조회
    @GetMapping("/my-page")
    @Operation(summary = "회원 정보 조회 - 마이페이지", description = "인증이 완료된 회원의 정보를 조회한다.")
    public ResponseEntity<UserInfoDto> userInfo(Principal principal) {

        String email = principal.getName();

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        User infoUser = userService.getUserByEmail(email);
        UserInfoDto responseUserInfoDto = new UserInfoDto(infoUser);

        return ResponseEntity.ok(responseUserInfoDto);
    }


    // 회원 탈퇴
    @DeleteMapping("/my-page/withdraw")
    @Operation(summary = "회원 탈퇴", description = "인증이 완료된 회원의 정보를 삭제한다.")
    public ResponseEntity<String> userDelete(Principal principal, @RequestParam String password) {

        String email = principal.getName();
        userService.delete(email, password);
        // UserInfoDto의 경우 password를 포함하고 있음 -> 이걸 프론트 단에서 처리할지, 아예 새롭게 Dto를 생성할지 고민

        return ResponseEntity.ok("탈퇴되었습니다.");
    }

    // 회원 정보 수정
    @PostMapping("/my-page")
    @Operation(summary = "회원 정보 수정", description = "인증이 완료된 회원의 정보를 수정한다.")
    public ResponseEntity<String> userModify(
            Principal principal,
            @RequestBody UserModifyDto userModifyDto){

        String userEmail = principal.getName();

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        userService.update(userEmail, userModifyDto);
        log.info("성공");

        return ResponseEntity.ok("회원 정보 수정이 완료되었습니다.");
    }

    // 비밀번호 수정
    @PostMapping("/my-page/pass-word")
    @Operation(summary = "비밀번호 수정", description = "인증이 완료된 회원의 비밀번호를 수정한다.")
    public ResponseEntity<String> userModifyPassword(
            Principal principal,
            @RequestBody PasswordModifyDto passwordModifyDto){

        String userEmail = principal.getName();

        JwtToken jwtToken = userService.modifyPassword(userEmail, passwordModifyDto);

        // 비밀번호 수정 후에 -> 로그인 페이지로 이동하도록 -> 그러면 토큰 정보는 필요 없지 않나?
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @GetMapping("/my-page/verify-pwd")
    public ResponseEntity<Boolean> userVerifyPwd(Principal principal,
                                                @RequestParam String oldPassword){
        String email = principal.getName();
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent() && passwordEncoder.matches(oldPassword, byEmail.get().getPassword())){
            return ResponseEntity.ok(true);    // 비밀번호 일치 시
        } else {
            return ResponseEntity.ok(false);    // 일치 안하면 ㅂㅂ
        }
    }

//    // 회원 목록 조회
//    @GetMapping("/admin/user-list")
//    public ResponseEntity<List<UserInfoDto>> adminGetUserList(){
//        return ResponseEntity.ok(userService.getAllUser());
//    }

    // 개인 계좌 등록
    @PostMapping("/payment")
    @Operation(summary = "회원 개인 계좌 개설", description = "회원 계좌 정보를 등록한다.")
    public ResponseEntity<String> createAccount(
            Principal principal,
            @Valid @RequestBody PersonalAccountDto personalAccountDto
    )
    {
        String userEmail = principal.getName();
        personalAccountService.personalAccountRegister(userEmail, personalAccountDto);

        return ResponseEntity.ok("계좌가 등록됐습니다.");
    }

    // 개인 계좌 삭제
    @DeleteMapping("/payment")
    @Operation(summary = "회원 개인 계좌 삭제", description = "회원 계좌를 삭제한다.")
    public ResponseEntity<String> deleteAccount(Principal principal)
    {
        String userEmail = principal.getName();
        personalAccountService.personalAccountDelete(userEmail);

        return ResponseEntity.ok("계좌가 삭제되었습니다.");
    }

    // 이메일 중복 확인 -> 단순 조회이기는 하지만
    @GetMapping("/email/exists")
    public ResponseEntity<Boolean> emailExists(@RequestParam String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            return ResponseEntity.ok(true);    // 이메일이 존재하면 true
        } else {
            return ResponseEntity.ok(false);    // 존재하지 않는 이메일이면 false
        }
    }
}
