package com.beyond3.yyGang.auth.service;

import com.beyond3.yyGang.auth.dto.JwtToken;

public interface AuthService {

    JwtToken signIn(String username, String password);
    JwtToken refresh(String token);
    void logout(String token);

}
