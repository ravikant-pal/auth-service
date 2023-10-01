package com.triton.auth.exceptions;

import com.triton.auth.exceptions.enums.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessingException extends RuntimeException {

    private ProcessingException(ErrorMessage errorMessage, Object... variables) {
        super(String.format(errorMessage.getMessage(), variables));
    }

    public static ProcessingException apiNotImplemented() {
        return new ProcessingException(ErrorMessage.API_NOT_IMPLEMENTED);
    }

    public static ProcessingException build() {
        return new ProcessingException(ErrorMessage.APPLICATION_PROCESSING_ERROR);
    }
}
