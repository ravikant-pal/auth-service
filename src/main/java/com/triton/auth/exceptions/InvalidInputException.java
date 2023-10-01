package com.triton.auth.exceptions;

import com.triton.auth.exceptions.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

    private InvalidInputException(ErrorMessage errorMessage, Object... variables) {
        super(String.format(errorMessage.getMessage(), variables));
    }

    public static InvalidInputException build(Object... input) {
        return new InvalidInputException(ErrorMessage.INVALID_INPUT,  input);
    }
}
