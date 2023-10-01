package com.triton.auth.exceptions;

import com.triton.auth.exceptions.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnauthorizedAccessException extends RuntimeException {

    private UnauthorizedAccessException(ErrorMessage errorMessage, Object... variables) {
        super(String.format(errorMessage.getMessage(), variables));
    }

    public static UnauthorizedAccessException build(Object... cause) {
        return new UnauthorizedAccessException(ErrorMessage.UNAUTHORIZED_ACCESS,cause);
    }
}
