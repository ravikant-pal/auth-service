package com.triton.auth.service.impl;

import com.google.common.collect.Maps;
import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.model.RefreshToken;
import com.triton.auth.repository.RefreshTokenRepository;
import com.triton.auth.service.TokenService;
import com.triton.auth.utils.TokenProvider;
import com.triton.mscommons.exceptions.InvalidJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public TokenServiceImpl(TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Tokens createAuthenticationTokens(String userId, String username, String email) {
        String accessToken = tokenProvider.generateToken(userId, username, Boolean.FALSE);
        String refreshToken = tokenProvider.generateToken(userId, username, Boolean.TRUE);
        refreshTokenRepository.save(RefreshToken.builder().refreshToken(refreshToken).email(email).build());
        return new Tokens(accessToken, refreshToken);
    }

    @Override
    public Map<String, String> getAccessTokenFromRefreshToken(String refreshToken) {
        Map<String, String> res = Maps.newHashMap();
        String email = tokenProvider.extractUsername(refreshToken);
        if (!StringUtils.isBlank(email) && getRefreshToken(email).equals(refreshToken)
                && tokenProvider.validateToken(refreshToken)) {
            String accessToken = tokenProvider.generateTokenFromRefreshToken(refreshToken);
            res.put("accessToken", accessToken);
        } else {
            log.error("Invalid Refresh token");
            throw InvalidJwtException.build();
        }
        return res;
    }

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    @Override
    public void clearRefreshToken(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }

    private String getRefreshToken(String email) {
        return refreshTokenRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .map(RefreshToken::getRefreshToken)
                .orElseGet(() -> {
                    log.warn("Refresh has been expired from DB !!");
                    return StringUtils.EMPTY;
                });
    }

}
