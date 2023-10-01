package com.triton.auth.dto.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tokens {
    private String accessToken;
    private String refreshToken;
}
