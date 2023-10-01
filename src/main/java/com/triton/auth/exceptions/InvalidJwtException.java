package com.triton.auth.exceptions;

import com.triton.auth.exceptions.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtException extends RuntimeException {

    private InvalidJwtException(ErrorMessage errorMessage, Object... variables) {
        super(String.format(errorMessage.getMessage(), variables));
    }

    public static InvalidJwtException build() {
        return new InvalidJwtException(ErrorMessage.INVALID_JWT_TOKEN);
    }
}
