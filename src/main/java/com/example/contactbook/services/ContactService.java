package com.example.contactbook.services;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.entities.Contact;

import java.util.List;

public interface ContactService {
    void add(ContactDto contactDto);

    void edit(Long id, ContactDto contactDto);

    void deleteById(Long contactId);

    void deleteByName(String contactName);

    List<ContactDto> getAll();
}
