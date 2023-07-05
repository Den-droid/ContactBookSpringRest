package com.example.contactbook.exceptions;

public class PhoneNumberFormatException extends RuntimeException {
    public PhoneNumberFormatException(String phoneNumber, String message) {
        super(String.format("Format error for phone number %s : %s", phoneNumber, message));
    }
}
