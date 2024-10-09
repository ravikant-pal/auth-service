package com.triton.auth.service.impl;

import com.triton.auth.service.CookieService;
import com.triton.mscommons.enums.ApplicationEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.triton.mscommons.utils.CommonUtils.getCurrentDomain;
import static com.triton.mscommons.utils.CommonUtils.isTargetEnvironment;


@Slf4j
@Service
public class CookieServiceImpl implements CookieService {

    private final Environment environment;

    @Value("${authy.cookie.max-age-in-days}")
    private int cookieMaxAge;

    @Autowired
    public CookieServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String buildRefreshCookie(String refreshToken, HttpServletRequest request, boolean delete) {
        boolean isDev = isTargetEnvironment(environment, ApplicationEnvironment.DEV);
        boolean isQA = isTargetEnvironment(environment, ApplicationEnvironment.QA);

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(Boolean.TRUE)
                .secure(!isDev) // for Local set To false
                .domain(getCurrentDomain(request))
                .path(isDev ? "/" : "/refresh");
        if (isDev || isQA) {
            builder.maxAge(Duration.ofMinutes(delete ? 0 : cookieMaxAge));
        } else {
            builder.maxAge(Duration.ofDays(delete ? 0 : cookieMaxAge));
        }
        builder.sameSite("None");
        return builder.build().toString();
    }
}
