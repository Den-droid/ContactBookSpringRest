package com.example.contactbook.exceptions;

public class UserException extends RuntimeException {
    public UserException(String username, String message) {
        super(String.format("Error for user %s : %s", username, message));
    }
}
