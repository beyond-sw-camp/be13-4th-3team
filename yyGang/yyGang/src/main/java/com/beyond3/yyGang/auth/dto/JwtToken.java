package com.beyond3.yyGang.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Data
@AllArgsConstructor
public class JwtToken {

    // 클라이언트에 토큰을 보내기 위한 DTO
    private String accessToken;     // accessToken
    private String refreshToken;    // refreshToken
}
