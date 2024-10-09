package com.triton.auth.utils;

import com.triton.auth.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class TokenProvider implements Serializable {

    private static final String ISS = "authy";
    private final Key SECRET_KEY;

    @Value("${authy.access-token.expiration-in-min}")
    private int accessTokenExpirationTime;

    @Value("${authy.refresh-token.expiration-in-min}")
    private int refreshTokenExpirationTime;


    public TokenProvider() {
        this.SECRET_KEY = generateSecretKey();
    }


    public boolean validateToken(String token) {
        return !isTokenExpired(token) && isIssuerValid(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isIssuerValid(String token) {
        return ISS.equals(extractIssuer(token));
    }

    private String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    public String extractUserId(String token) {
        return extractClaim(token, "userId", String.class);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public <T> T extractClaim(String token, String claimName, Class<T> targetType) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName, targetType);
    }

    public String generateToken(String userId, String username, boolean isRefresh) {
        return doGenerateToken(userId, username, isRefresh, StringUtils.EMPTY);
    }

    // For Issue Access token from refresh token
    public String generateTokenFromRefreshToken(String refreshToken) {
        String sub = extractUsername(refreshToken);
        return doGenerateToken(StringUtils.EMPTY, sub, Boolean.FALSE, refreshToken);
    }

    private String doGenerateToken(String userId, String subject, boolean isRefresh, String refreshToken) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder()
                .setHeader(AppConfig.getTokenHeader())
                .setSubject(subject);
        if (StringUtils.isEmpty(refreshToken)) {
            builder.claim("userId",userId);
        } else {
            builder.addClaims(extractAllClaims(refreshToken));
        }
        return builder.setIssuer(ISS)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .signWith(SECRET_KEY,SignatureAlgorithm.HS512)
                .setExpiration(Date.from(getExpirationTime(now, isRefresh)))
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key generateSecretKey() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AppConfig.HMACAlgorithms.HS512);
            keyGenerator.init(AppConfig.HMACAlgorithms.KeySize.Five12, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encodedKey = secretKey.getEncoded();
            return new SecretKeySpec(encodedKey, AppConfig.HMACAlgorithms.HS512);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm", e);
            return null;
        }
    }

    private Instant getExpirationTime(Instant now, boolean isRefresh) {
        Instant expirationTime;
        if (isRefresh) {
            expirationTime = now.plusSeconds(refreshTokenExpirationTime * 60L);
        } else {
            expirationTime = now.plusSeconds(accessTokenExpirationTime * 60L);
        }
        return expirationTime;
    }


}
