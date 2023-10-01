package com.triton.auth.exceptions;

import com.triton.auth.exceptions.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private BadRequestException(ErrorMessage errorMessage, Object... variables) {
        super(String.format(errorMessage.getMessage(), variables));
    }

    public static BadRequestException build(Object... input) {
        return new BadRequestException(ErrorMessage.INVALID_INPUT, input);
    }
}
