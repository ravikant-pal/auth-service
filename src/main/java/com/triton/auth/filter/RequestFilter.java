package com.triton.auth.filter;


import com.triton.auth.service.UserService;
import com.triton.auth.utils.TokenProvider;
import com.triton.mscommons.exceptions.InvalidJwtException;
import com.triton.mscommons.exceptions.UnauthorizedAccessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.triton.auth.config.AppConfig.publicRoutes;
import static com.triton.auth.utils.Constants.*;
import static com.triton.mscommons.utils.Constants.FORWARD_SLASH;
import static com.triton.mscommons.utils.Constants.LOGGED_IN_USER_KEY;
import static com.triton.mscommons.utils.Constants.SwaggerConfig.SWAGGER_API_ENDPOINT;
import static com.triton.mscommons.utils.Constants.SwaggerConfig.SWAGGER_UI_ENDPOINT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    public RequestFilter(TokenProvider tokenProvider, UserService userService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String currentPath = requestURI.substring(requestURI.lastIndexOf(FORWARD_SLASH));
        try {
            if (requestURI.startsWith(SWAGGER_API_ENDPOINT) || requestURI.startsWith(SWAGGER_UI_ENDPOINT) || publicRoutes.contains(currentPath)) {
                chain.doFilter(request, response);
            } else {
                final String header = request.getHeader(AUTHORIZATION);
                if (!StringUtils.isEmpty(header) && header.startsWith(TOKEN_PREFIX)) {
                    String authToken = header.substring(7);
                    try {
                        boolean isValid = tokenProvider.validateToken(authToken);
                        if (!isValid) throw InvalidJwtException.build();
                        String loggedInUserId = tokenProvider.extractUserId(authToken);
                        request.setAttribute(LOGGED_IN_USER_KEY, userService.getById(loggedInUserId));
                        chain.doFilter(request, response);
                    } catch (RuntimeException e) {
                        log.error("Error occurred while validating Token ", e);
                        throw InvalidJwtException.build();
                    }
                } else {
                    log.error("Couldn't find bearer string");
                    throw UnauthorizedAccessException.build();
                }
            }
        } catch (Exception e) {
            this.exceptionResolver.resolveException(request, response, null, e);
        }

    }
}



