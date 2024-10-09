package com.triton.auth.config;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public  interface AppConfig {
    List<String> publicRoutes = Arrays.asList("/send-otp", "/verify-otp", "/refresh", "/logout");

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

    static Map<String, Object> getTokenHeader() {
        Map<String, Object> header = Maps.newHashMap();
        header.put("alg", "HS512");
        header.put("type", "JWT");
        return header;
    }
}
