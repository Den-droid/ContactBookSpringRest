package com.example.contactbook.services.impl;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.Email;
import com.example.contactbook.entities.PhoneNumber;
import com.example.contactbook.entities.User;
import com.example.contactbook.mappers.ContactMapper;
import com.example.contactbook.repositories.UserRepository;
import com.example.contactbook.security.user_details.UserDetailsImpl;
import com.example.contactbook.services.ContactService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private final UserRepository userRepository;

    private final ContactMapper contactMapper;

    public ContactServiceImpl(UserRepository userRepository,
                              ContactMapper contactMapper) {
        this.userRepository = userRepository;
        this.contactMapper = contactMapper;
    }

    public MessageResponseDto addContact(ContactDto contactDto) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        Contact contact = contactMapper.mapContact(
                contactDto.contactName(),
                contactDto.emails(),
                contactDto.phoneNumbers()
        );

        user.addContact(contact);
        userRepository.save(user);

        return new MessageResponseDto("User added successfully!");
    }

    public MessageResponseDto editContact(Long id, ContactDto contactDto) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        Contact editedContact = contactMapper.mapContact(
                contactDto.contactName(),
                contactDto.emails(),
                contactDto.phoneNumbers()
        );

        user.editContact(id, editedContact);
        userRepository.save(user);

        return new MessageResponseDto("User edited successfully!");
    }

    public MessageResponseDto deleteContact(Long contactId) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        user.deleteContact(contactId);
        userRepository.save(user);

        return new MessageResponseDto("User deleted successfully!");
    }

    public MessageResponseDto deleteContact(String contactName) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        user.deleteContact(contactName);
        userRepository.save(user);

        return new MessageResponseDto("User deleted successfully!");
    }

    public List<ContactDto> getAllContacts() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        List<Contact> contacts = user.getContactList();

        return contacts.stream()
                .map(x -> new ContactDto(
                        x.getContactName(),
                        x.getEmails().stream()
                                .map(Email::getEmail)
                                .collect(Collectors.toList()),
                        x.getPhoneNumbers().stream()
                                .map(PhoneNumber::getPhoneNumber)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
