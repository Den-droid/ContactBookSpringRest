package com.example.contactbook.exceptions;

public class EmailFormatException extends RuntimeException {
    public EmailFormatException(String email, String message) {
        super(String.format("Format error for email %s : %s", email, message));
    }
}
