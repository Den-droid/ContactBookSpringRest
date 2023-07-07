package com.example.contactbook.contact;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.entities.*;
import com.example.contactbook.entities.enums.EnumRole;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class GetContactTest {
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
    }

    @Test
    @Transactional
    public void getAllContactsSuccess() {
        Set<Email> emails = new HashSet<>();
        emails.add(new Email("american@gmail.com"));

        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        phoneNumbers.add(new PhoneNumber("+380967899232"));

        List<ContactDto> expectedContacts = new ArrayList<>();
        expectedContacts.add(new ContactDto("Ivanko",
                emails.stream()
                        .map(Email::getEmail).collect(Collectors.toList()),
                phoneNumbers.stream()
                        .map(PhoneNumber::getPhoneNumber).collect(Collectors.toList())));

        LoginDto loginDto = new LoginDto("user", "password");
        authenticationService.authenticateUser(loginDto);

        List<ContactDto> actualContacts = contactService.getAll();

        assertEquals(actualContacts, expectedContacts);
    }

}
