package com.example.contactbook.contact;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.entities.*;
import com.example.contactbook.entities.enums.EnumRole;
import com.example.contactbook.exceptions.ContactException;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.exceptions.PhoneNumberFormatException;
import com.example.contactbook.repositories.RoleRepository;
import com.example.contactbook.repositories.UserRepository;
import com.example.contactbook.services.AuthenticationService;
import com.example.contactbook.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class AddContactTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ContactService contactService;

    @BeforeEach
    public void initTestCase() {
        Optional<Role> optionalRole = roleRepository.findByName(EnumRole.ROLE_USER);
        Role role = optionalRole.orElseGet(() -> new Role(EnumRole.ROLE_USER));
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user = new User("user", "user@gmail.com", encoder.encode("password"));
        user.setRoles(roleSet);

        User userWithoutContacts =
                new User("user2", "user2@gmail.com", encoder.encode("password"));
        userWithoutContacts.setRoles(roleSet);

        Contact contact = new Contact();
        contact.setContactName("Ivanko");

        Set<Email> emails = new HashSet<>();
        emails.add(new Email("american@gmail.com"));

        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        phoneNumbers.add(new PhoneNumber("+380967899232"));

        contact.setPhoneNumbers(phoneNumbers);
        contact.setEmails(emails);

        user.addContact(contact);

        userRepository.save(user);
        userRepository.save(userWithoutContacts);
    }

    @Test
    @Transactional
    public void addContactFailure_ContactWithNameAlreadyExists() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967899232");

        ContactDto contactDto = new ContactDto("Ivanko", emails, phoneNumbers);

        assertThrows(ContactException.class, () -> contactService.add(contactDto));
    }

    @Test
    @Transactional
    public void addContactFailure_BadContactNameFormat() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967899232");

        ContactDto contactDto = new ContactDto("      ", emails, phoneNumbers);

        assertThrows(ContactException.class, () -> contactService.add(contactDto));
    }

    @Test
    @Transactional
    public void addContactFailure_WrongEmailFormat() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967899232");

        ContactDto contactDto = new ContactDto("Ivan", emails, phoneNumbers);

        assertThrows(EmailFormatException.class, () -> contactService.add(contactDto));
    }

    @Test
    @Transactional
    public void addContactFailure_WrongPhoneNumberFormat() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967sdfsd9232");

        ContactDto contactDto = new ContactDto("Ivan", emails, phoneNumbers);

        assertThrows(PhoneNumberFormatException.class, () -> contactService.add(contactDto));
    }

    @Test
    @Transactional
    public void addContactSuccess() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967923298");

        ContactDto contactDto = new ContactDto("Ivan", emails, phoneNumbers);

        MessageResponseDto actualResponse = contactService.add(contactDto);
        MessageResponseDto expectedResponse = new MessageResponseDto("User added successfully!");

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @Transactional
    public void addContactSuccess_addToAnotherUserWithTheSameContactName() {
        LoginDto loginDto = new LoginDto("user2", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("american@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967923298");

        ContactDto contactDto = new ContactDto("Ivanko", emails, phoneNumbers);

        MessageResponseDto actualResponse = contactService.add(contactDto);
        MessageResponseDto expectedResponse = new MessageResponseDto("User added successfully!");

        assertEquals(actualResponse, expectedResponse);
    }
}
