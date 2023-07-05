package com.example.contactbook.entities;

import com.example.contactbook.exceptions.ContactException;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 35)
    private String username;

    @Column(length = 75)
    private String email;

    @Column(length = 100)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Contact> contactList = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void addContact(Contact contact) {
        boolean notExists = contactList.stream()
                .noneMatch(x -> x.getContactName().equals(contact.getContactName()));

        if (notExists) {
            contactList.add(contact);
        } else {
            throw new ContactException(contact.getContactName(),
                    "Contact with such name already exists!");
        }
    }

    public void editContact(Long id, Contact editedContact) {
        Optional<Contact> contact = contactList.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (contact.isPresent()) {
            contact.get().setContactName(editedContact.getContactName());

            contact.get().getEmails().clear();
            contact.get().getEmails().addAll(editedContact.getEmails());

            contact.get().getPhoneNumbers().clear();
            contact.get().getPhoneNumbers().addAll(editedContact.getPhoneNumbers());
        } else {
            throw new ContactException(id, "Contact with such id doesn't exist!");
        }
    }

    public void deleteContact(Long id) {
        Optional<Contact> contact = contactList.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (contact.isPresent()) {
            contactList.remove(contact.get());
        } else {
            throw new ContactException(id, "Contact with such id doesn't exist!");
        }
    }

    public void deleteContact(String contactName) {
        Optional<Contact> contact = contactList.stream()
                .filter(x -> x.getContactName().equals(contactName))
                .findFirst();

        if (contact.isPresent()) {
            contactList.remove(contact.get());
        } else {
            throw new ContactException(contactName, "Contact with such name doesn't exist!");
        }
    }

}
