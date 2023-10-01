package com.triton.auth.service.impl;

import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.model.User;
import com.triton.auth.service.AuthService;
import com.triton.auth.service.TokenService;
import com.triton.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    @Autowired
    public AuthServiceImpl(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean isAuthenticated(String refreshToken) {
        return tokenService.validateToken(refreshToken);
    }


    @Override
    public boolean logout(String email) {
         tokenService.clearRefreshToken(email);
        //todo::clear cookie
        return Boolean.TRUE;
    }

    @Override
    public Tokens doLogin(String email) {
        User user = userService.getOrCreateUser(email);
        return tokenService.createAuthenticationTokens(user.getId(), user.getUsername(), email);
    }


}
