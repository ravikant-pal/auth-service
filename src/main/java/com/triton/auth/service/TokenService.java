package com.triton.auth.service;

import com.triton.auth.dto.helper.Tokens;

import java.util.Map;

public interface TokenService {

    /**
     * This is for NoSQL
     * @param userId
     * @param roles
     * @param sub
     * @param email
     * @return Tokens
     */
    Tokens createAuthenticationTokens(String userId, String username, String email);

    boolean validateToken(String token);

    void clearRefreshToken(String key);

    Map<String, String> getAccessTokenFromRefreshToken(String refreshToken);
}
