package com.example.contactbook.mappers;

import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.Email;
import com.example.contactbook.entities.PhoneNumber;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.exceptions.PhoneNumberFormatException;
import com.example.contactbook.validators.EmailValidator;
import com.example.contactbook.validators.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapper {
    private final PhoneNumberValidator phoneNumberValidator;

    private final EmailValidator emailValidator;

    public ContactMapper(PhoneNumberValidator phoneNumberValidator,
                         EmailValidator emailValidator) {
        this.phoneNumberValidator = phoneNumberValidator;
        this.emailValidator = emailValidator;
    }

    public Contact mapContact(String contactName, List<String> emails, List<String> phoneNumbers) {
        Contact contact = new Contact();

        contact.setContactName(contactName);
        contact.setEmails(emails.stream()
                .filter(x -> {
                    if (emailValidator.validateWhole(x))
                        return true;
                    else throw new EmailFormatException(x, "Bad format!");
                })
                .map(Email::new)
                .collect(Collectors.toSet()));
        contact.setPhoneNumbers(phoneNumbers.stream()
                .filter(x -> {
                    if (phoneNumberValidator.validateWhole(x))
                        return true;
                    else throw new PhoneNumberFormatException(x, "Bad format");
                })
                .map(PhoneNumber::new)
                .collect(Collectors.toSet()));

        return contact;
    }
}
