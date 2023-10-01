package com.triton.auth.advice;

import com.triton.auth.exceptions.*;
import com.triton.auth.exceptions.dto.ExceptionResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {

    /**
     * Put all the handlers alphabetically
     **/

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtException(InvalidJwtException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleProcessingException(ProcessingException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .path(getCurrentResourcePath(request))
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getCurrentResourcePath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
