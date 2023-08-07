package com.example.contactbook.mappers;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.Email;
import com.example.contactbook.entities.PhoneNumber;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.exceptions.PhoneNumberFormatException;
import com.example.contactbook.validators.EmailValidator;
import com.example.contactbook.validators.PhoneNumberValidator;
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

    public Contact mapToContact(ContactDto dto) {
        Contact contact = new Contact();

        contact.setContactName(dto.contactName());
        contact.setEmails(dto.emails().stream()
                .filter(email -> {
                    if (emailValidator.validateWhole(email))
                        return true;
                    else throw new EmailFormatException(email, "Bad format!");
                })
                .map(email -> new Email(email, contact))
                .collect(Collectors.toSet()));
        contact.setPhoneNumbers(dto.phoneNumbers().stream()
                .filter(phoneNumber -> {
                    if (phoneNumberValidator.validateWhole(phoneNumber))
                        return true;
                    else throw new PhoneNumberFormatException(phoneNumber, "Bad format");
                })
                .map(phoneNumber -> new PhoneNumber(phoneNumber, contact))
                .collect(Collectors.toSet()));

        return contact;
    }

    public ContactDto mapFromContact(Contact contact) {
        String name = contact.getContactName();
        List<String> emails = contact.getEmails().stream()
                .map(Email::getEmail)
                .collect(Collectors.toList());
        List<String> phoneNumbers = contact.getPhoneNumbers().stream()
                .map(PhoneNumber::getPhoneNumber)
                .collect(Collectors.toList());

        return new ContactDto(name, emails, phoneNumbers);
    }

}
