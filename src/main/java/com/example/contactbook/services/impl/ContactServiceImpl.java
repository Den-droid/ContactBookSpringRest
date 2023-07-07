package com.example.contactbook.services.impl;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.Email;
import com.example.contactbook.entities.PhoneNumber;
import com.example.contactbook.entities.User;
import com.example.contactbook.exceptions.ContactException;
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

    @Override
    public MessageResponseDto add(ContactDto contactDto) {
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

    @Override
    public MessageResponseDto edit(Long contactId, ContactDto contactDto) {
        if (contactId < 1) {
            throw new ContactException(contactId, "Contact id less than zero!");
        }

        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        Contact editedContact = contactMapper.mapContact(
                contactDto.contactName(),
                contactDto.emails(),
                contactDto.phoneNumbers()
        );

        user.editContact(contactId, editedContact);
        userRepository.save(user);

        return new MessageResponseDto("User edited successfully!");
    }

    @Override
    public MessageResponseDto deleteById(Long contactId) {
        if (contactId < 1) {
            throw new ContactException(contactId, "Contact id less than zero!");
        }

        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        user.deleteContact(contactId);
        userRepository.save(user);

        return new MessageResponseDto("User deleted successfully!");
    }

    @Override
    public MessageResponseDto deleteByName(String contactName) {
        if (contactName.isEmpty() || contactName.isBlank()) {
            throw new ContactException(contactName, "Wrong contact name format!");
        }

        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).get();

        user.deleteContact(contactName);
        userRepository.save(user);

        return new MessageResponseDto("User deleted successfully!");
    }

    @Override
    public List<ContactDto> getAll() {
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
