package com.example.contactbook.exceptions;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(String contactName, String message) {
        super(String.format("Error for contact %s : %s", contactName, message));
    }

    public ContactNotFoundException(Long id, String message) {
        super(String.format("Error for contact with id %s : %s", id, message));
    }
}
