package com.example.contactbook.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username, String message) {
        super(String.format("Error for user %s : %s", username, message));
    }
}
