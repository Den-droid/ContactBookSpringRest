package com.example.contactbook.services;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;

import java.util.List;

public interface ContactService {
    MessageResponseDto add(ContactDto contactDto);

    MessageResponseDto edit(Long id, ContactDto contactDto);

    MessageResponseDto deleteById(Long contactId);

    MessageResponseDto deleteByName(String contactName);

    List<ContactDto> getAll();
}
