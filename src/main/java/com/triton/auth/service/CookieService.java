package com.triton.auth.service;


import jakarta.servlet.http.HttpServletRequest;

public interface CookieService {
    String buildRefreshCookie(String refreshToken, HttpServletRequest request, boolean delete);
}
