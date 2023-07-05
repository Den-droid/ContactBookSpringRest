package com.example.contactbook.exceptions;

public class ContactException extends RuntimeException {
    public ContactException(String contactName, String message) {
        super(String.format("Error for contact %s : %s", contactName, message));
    }

    public ContactException(Long id, String message) {
        super(String.format("Error for contact with id %s : %s", id, message));
    }
}
