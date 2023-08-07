package com.example.contactbook.controllers;

import com.example.contactbook.dto.ContactDto;
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
        contactService.add(contactDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editContact(@RequestBody ContactDto contactDto,
                                         @PathVariable Long id) {
        contactService.edit(id, contactDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteContact(@RequestParam String name) {
        contactService.deleteByName(name);
        return ResponseEntity.status(204).build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        List<ContactDto> contacts = contactService.getAll();
        return ResponseEntity.ok(contacts);
    }
}
