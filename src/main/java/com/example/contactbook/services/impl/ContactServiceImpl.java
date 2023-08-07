package com.example.contactbook.services.impl;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.User;
import com.example.contactbook.exceptions.ContactAlreadyExistsException;
import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.mappers.ContactMapper;
import com.example.contactbook.repositories.ContactRepository;
import com.example.contactbook.security.utils.SessionUtil;
import com.example.contactbook.services.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final SessionUtil sessionUtil;

    public ContactServiceImpl(ContactRepository contactRepository,
                              ContactMapper contactMapper,
                              SessionUtil sessionUtil) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.sessionUtil = sessionUtil;
    }

    @Override
    public void add(ContactDto contactDto) {
        User user = sessionUtil.getUserFromSession();

        if (contactRepository.existsByUserAndContactName(user, contactDto.contactName())) {
            throw new ContactAlreadyExistsException(contactDto.contactName(),
                    "Contact with such name already exists!");
        }

        Contact contact = contactMapper.mapToContact(contactDto);
        contact.setUser(user);

        contactRepository.save(contact);
    }

    @Override
    public void edit(Long contactId, ContactDto contactDto) {
        User user = sessionUtil.getUserFromSession();

        if (!contactRepository.existsByUserAndId(user, contactId)) {
            throw new ContactNotFoundException(contactId, "Contact with such id doesn't exist!");
        }

        if (contactRepository.
                existsByUserAndContactNameAndIdNot(user, contactDto.contactName(), contactId)) {
            throw new ContactAlreadyExistsException(contactDto.contactName(),
                    "Contact with such name already exists!");
        }

        Contact editedContact = contactMapper.mapToContact(contactDto);
        editedContact.setId(contactId);
        editedContact.setUser(user);

        contactRepository.save(editedContact);
    }

    @Override
    public void deleteById(Long contactId) {
        User user = sessionUtil.getUserFromSession();

        if (!contactRepository.existsByUserAndId(user, contactId)) {
            throw new ContactNotFoundException(contactId, "Contact with such id doesn't exist!");
        }

        contactRepository.deleteById(contactId);
    }

    @Override
    public void deleteByName(String contactName) {
        User user = sessionUtil.getUserFromSession();

        Contact contact = contactRepository.findByUserAndContactName(user, contactName).orElseThrow(
                () -> new ContactNotFoundException(contactName, "Contact with such name doesn't exist!"));

        contactRepository.delete(contact);
    }

    @Override
    public List<ContactDto> getAll() {
        User user = sessionUtil.getUserFromSession();

        List<Contact> contacts = user.getContactList();

        return contacts.stream()
                .map(contactMapper::mapFromContact)
                .collect(Collectors.toList());
    }
}
