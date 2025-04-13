package com.beyond3.yyGang.user.dto;

import com.beyond3.yyGang.user.domain.Gender;
import com.beyond3.yyGang.user.domain.Role_name;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserModifyDto {
    // 회원 정보 수정은 일단 비밀번호 변경 없이

    private String name;

    private Role_name role;

    private LocalDate birthday;

    private Gender gender;  // 성별 -> MALE, FEMALE

    private String phone; // 전화번호

    private String address; // 주소

//    private String profileImageUrl;
}
