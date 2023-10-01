package com.triton.auth.utils;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface Constants {

    // Others
    String EMAIL_ID = "water.bottle@gmail.com";
    String MAIL_OTP_SUBJECT = "OTP for login to your account - Water.Bottle";
    String EMAIL_SENT_SUCCESS_MSG = "Email send successfully.";
    String WELCOME_ADMIN_MESSAGE = "OHH! MY GOT, You are Admin.";
    String OTP_TEMPLATE = "email/otp";
    String DEFAULT_USER_NAME = "Unknown";
    String TOKEN_PREFIX = "Bearer";
    String LOGGED_IN_USER_KEY = "user";
    String COMMA = ",";
    String FORWARD_SLASH = "/";
    String API_V1 = "api/v1";
    String API_V2 = "api/v2";

    /**
     * Swagger End points
     */

    interface SwaggerConfig {
        String API_ENDPOINT = "/v3/api-docs";
        String UI_ENDPOINT = "/swagger-ui";
    }

    /**
     * Security Algorithms
     */
    interface HMACAlgorithms {
        String HS512 = "HmacSHA512";

        interface KeySize {
            int Two56 = 256;
            int Five12 = 512;
        }
    }

    /**
     * Application Configuration
     */
    interface AppConfig {
        List<String> publicRoutes = Arrays.asList("/send-otp", "/verify-otp", "/refresh", "/logout");
    }


    static Map<String, Object> getTokenHeader() {
        Map<String, Object> header = Maps.newHashMap();
        header.put("alg", "HS512");
        header.put("type", "JWT");
        return header;
    }
}
