package com.example.contactbook.exceptions.handlers;

import com.example.contactbook.dto.ErrorMessageDto;
import com.example.contactbook.exceptions.ContactAlreadyExistsException;
import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.exceptions.PhoneNumberFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ContactControllerAdvice {
    @ExceptionHandler(value = ContactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageDto handleContactException(ContactNotFoundException ex, WebRequest request) {
        return new ErrorMessageDto(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now().toString(),
                ex.getMessage());
    }

    @ExceptionHandler(value = ContactAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleContactException(ContactAlreadyExistsException ex, WebRequest request) {
        return new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString(),
                ex.getMessage());
    }

    @ExceptionHandler(value = EmailFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handleEmailFormatException(EmailFormatException ex, WebRequest request) {
        return new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString(),
                ex.getMessage());
    }

    @ExceptionHandler(value = PhoneNumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handlePhoneNumberException(PhoneNumberFormatException ex, WebRequest request) {
        return new ErrorMessageDto(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().toString(),
                ex.getMessage());
    }
}
