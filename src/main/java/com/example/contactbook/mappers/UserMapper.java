package com.example.contactbook.mappers;

import com.example.contactbook.entities.User;
import com.example.contactbook.exceptions.EmailFormatException;
import com.example.contactbook.validators.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final EmailValidator emailValidator;
    private final PasswordEncoder encoder;

    public UserMapper(EmailValidator emailValidator,
                      PasswordEncoder encoder) {
        this.emailValidator = emailValidator;
        this.encoder = encoder;
    }

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
