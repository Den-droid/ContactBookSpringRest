package com.example.contactbook.mappers;

import com.example.contactbook.dto.auth.SignupDto;
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

    public User mapToUser(SignupDto dto) {
        User user = new User();

        user.setUsername(dto.username());

        if (!emailValidator.validateWhole(dto.email())) {
            throw new EmailFormatException(dto.email(), "Bad format!");
        }

        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));

        return user;
    }
}
