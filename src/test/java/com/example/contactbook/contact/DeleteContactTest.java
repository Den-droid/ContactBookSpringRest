package com.example.contactbook.contact;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.entities.*;
import com.example.contactbook.entities.enums.EnumRole;
import com.example.contactbook.exceptions.ContactException;
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
public class DeleteContactTest {
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

        User user2 = new User("user2", "user2@gmail.com", encoder.encode("password"));
        user2.setRoles(roleSet);

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
        userRepository.save(user2);
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactWithIdDoesntExist() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                1_000_000L
        ));
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactWithBadId() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                -1L
        ));
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactIdFromAnotherUser() {
        LoginDto loginDto = new LoginDto("user2", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("america@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967923278");

        ContactDto contactDto = new ContactDto("Ivan", emails, phoneNumbers);

        contactService.addContact(contactDto);

        User user = userRepository.findByUsername("user2").get();
        long id = user.getContactList().get(0).getId();

        loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                id
        ));
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactWithNameDoesntExist() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                "dfnoshfndsdafsd"
        ));
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactWithBadNameFormat() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                "        "
        ));
    }

    @Test
    @Transactional
    public void deleteContactFailure_ContactNameFromAnotherUser() {
        LoginDto loginDto = new LoginDto("user2", "password");
        authenticationService.authenticateUser(loginDto);

        List<String> emails = new ArrayList<>();
        emails.add("america@gmail.com");

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("+380967923278");

        ContactDto contactDto = new ContactDto("Ivan", emails, phoneNumbers);

        contactService.addContact(contactDto);

        User user = userRepository.findByUsername("user2").get();
        String contactName = user.getContactList().get(0).getContactName();

        loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        assertThrows(ContactException.class, () -> contactService.deleteContact(
                contactName
        ));
    }

    @Test
    @Transactional
    public void deleteContactByIdSuccess() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        User user = userRepository.findByUsername("user").get();
        Contact existedContact = user.getContactList().get(0);

        MessageResponseDto actualResponse =
                contactService.deleteContact(existedContact.getId());
        MessageResponseDto expectedResponse = new MessageResponseDto("User deleted successfully!");

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @Transactional
    public void deleteContactByNameSuccess() {
        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        User user = userRepository.findByUsername("user").get();
        Contact existedContact = user.getContactList().get(0);

        MessageResponseDto actualResponse =
                contactService.deleteContact(existedContact.getContactName());
        MessageResponseDto expectedResponse = new MessageResponseDto("User deleted successfully!");

        assertEquals(actualResponse, expectedResponse);
    }
}
