package com.triton.auth.service;

import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.dto.request.OtpAuthRequest;

import java.util.Map;

public interface AuthService {

    boolean isAuthenticated(String refreshToken);

    boolean logout(String email);

    Tokens doLogin(String email);
}
