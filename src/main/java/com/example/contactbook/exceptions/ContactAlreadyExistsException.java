package com.example.contactbook.exceptions;

public class ContactAlreadyExistsException extends RuntimeException {
    public ContactAlreadyExistsException(String contactName, String message) {
        super(String.format("Error for contact %s : %s", contactName, message));
    }
}
