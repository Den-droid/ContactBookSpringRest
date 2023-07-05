package com.example.contactbook.mappers;

import com.example.contactbook.entities.User;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordEncoder encoder;

    public User mapUser(String username, String email, String password) {
        User user = new User();

        user.setUsername(username);

        if (!emailValidator.validateWhole(email)) {
            throw new EmailFormatException(email, "Bad format!");
        }

        user.setEmail(email);
        user.setPassword(encoder.encode(password));

        return user;
    }
}
