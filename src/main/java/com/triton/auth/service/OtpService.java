package com.triton.auth.service;

import com.triton.auth.dto.request.OtpAuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface OtpService {

    String sendOtp(String email);

    Map<String, String> verifyOtp(OtpAuthRequest otpAuthRequest, HttpServletRequest request, HttpServletResponse response);
}
