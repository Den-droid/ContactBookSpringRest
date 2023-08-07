package com.example.contactbook.security.utils;

import com.example.contactbook.entities.User;
import com.example.contactbook.exceptions.UserNotFoundException;
import com.example.contactbook.repositories.UserRepository;
import com.example.contactbook.security.user_details.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {
    private final UserRepository userRepository;

    public SessionUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromSession() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username, "User by specified username was not found!"));
    }

}
