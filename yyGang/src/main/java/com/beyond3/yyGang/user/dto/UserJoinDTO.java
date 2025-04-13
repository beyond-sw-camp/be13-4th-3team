package com.beyond3.yyGang.user.dto;

import com.beyond3.yyGang.user.domain.Gender;
import com.beyond3.yyGang.user.domain.Role_name;
import com.beyond3.yyGang.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserJoinDTO {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPwd; // 비밀번호

    @NotBlank(message = "비밀번호를 확인은 필수입니다.")
    private String userPwd2;  // 비밀번호 확인

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    private LocalDate birthday;

    @NotNull(message = "성별 선택은 필수 입니다.")  // ENUM 타입엔 NotBlank 사용
    private Gender gender;  // 성별 -> MALE, FEMALE

    private String phone; // 전화번호

    private String address; // 주소

//    private String profileImageUrl;

    public Boolean passwordMathcing(){
        return this.userPwd.equals(this.userPwd2);
    }

    public User toEntity(){

        return User.builder()
                .email(email)
                .password(userPwd)
                .role(Role_name.CUSTOMER) // Role은 가입 시 무조건 CUSTOMER로
                .name(name)
                .birthday(birthday)
                .gender(gender)
                .phone(phone)
                .address(address)
//                .profileImageUrl(profileImageUrl)
                .build();
    }
}
