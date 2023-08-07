package com.example.contactbook.repositories;

import com.example.contactbook.entities.Contact;
import com.example.contactbook.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContactRepository extends CrudRepository<Contact, Long> {
    Boolean existsByUserAndId(User user, Long id);

    Boolean existsByUserAndContactName(User user, String contactName);

    Boolean existsByUserAndContactNameAndIdNot(User user, String contactName, Long id);

    Optional<Contact> findByUserAndContactName(User user, String contactName);
}
