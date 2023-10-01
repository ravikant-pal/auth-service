package com.triton.auth.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.triton.auth.dto.helper.Tokens;
import com.triton.auth.model.Role;
import com.triton.auth.model.User;
import com.triton.auth.service.TokenService;
import com.triton.auth.utils.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private static final Integer REFRESH_TOKEN_EXPIRE_TIME = 6;
    private final TokenProvider tokenProvider;
    private final LoadingCache<String, String> refreshTokenCache;

    @Autowired
    public TokenServiceImpl(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@Nonnull String s) {
                        return StringUtils.EMPTY;
                    }
                });
    }

    @Override
    public Tokens createAuthenticationTokens(String userId, String username, String email) {
        String accessToken = tokenProvider.generateToken(userId, username, Boolean.FALSE);
        String refreshToken = tokenProvider.generateToken(userId, username, Boolean.TRUE);
        refreshTokenCache.put(email, refreshToken);
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
        }
        return res;
    }

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    @Override
    public void clearRefreshToken(String key) {
        refreshTokenCache.invalidate(key);
    }

    private String getRefreshToken(String key) {
        try {
            return refreshTokenCache.get(key);
        } catch (Exception e) {
            log.error("Error getting refresh token from cache!!");
            return StringUtils.EMPTY;
        }
    }

}
