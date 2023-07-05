package com.example.contactbook.services;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;

import java.util.List;

public interface ContactService {
    MessageResponseDto addContact(ContactDto contactDto);

    MessageResponseDto editContact(Long id, ContactDto contactDto);

    MessageResponseDto deleteContact(Long contactId);

    MessageResponseDto deleteContact(String contactName);

    List<ContactDto> getAllContacts();
}
