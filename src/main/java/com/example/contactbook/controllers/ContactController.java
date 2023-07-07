package com.example.contactbook.controllers;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.services.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addContact(@RequestBody ContactDto contactDto) {
        try {
            MessageResponseDto messageResponseDto = contactService.add(contactDto);
            return ResponseEntity.ok(messageResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editContact(@RequestBody ContactDto contactDto,
                                         @PathVariable Long id) {
        try {
            MessageResponseDto messageResponseDto = contactService.edit(id, contactDto);
            return ResponseEntity.ok(messageResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            MessageResponseDto messageResponseDto = contactService.deleteById(id);
            return ResponseEntity.ok(messageResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteContact(@RequestParam String name) {
        try {
            MessageResponseDto messageResponseDto = contactService.deleteByName(name);
            return ResponseEntity.ok(messageResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        List<ContactDto> contacts = contactService.getAll();
        return ResponseEntity.ok(contacts);
    }
}
