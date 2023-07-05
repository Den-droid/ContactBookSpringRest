package com.example.contactbook.controllers.advice;

import com.example.contactbook.dto.response.TokenRefreshErrorMessage;
import com.example.contactbook.exceptions.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public TokenRefreshErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new TokenRefreshErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                LocalDate.now(),
                ex.getMessage());
    }
}
