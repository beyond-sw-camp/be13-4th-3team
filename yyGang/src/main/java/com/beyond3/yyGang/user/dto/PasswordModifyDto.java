package com.beyond3.yyGang.user.dto;

import lombok.Data;

@Data
public class PasswordModifyDto {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
