package com.example.contactbook.authentication.services;

import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.entities.Role;
import com.example.contactbook.entities.User;
import com.example.contactbook.entities.enums.EnumRole;
import com.example.contactbook.repositories.RoleRepository;
import com.example.contactbook.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void initTestCase() {
        Optional<Role> optionalRole = roleRepository.findByName(EnumRole.ROLE_USER);
        Role role = optionalRole.orElseGet(() -> new Role(EnumRole.ROLE_USER));
        roleRepository.save(role);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user = new User("user", "user@gmail.com", encoder.encode("password"));
        user.setRoles(roleSet);

        userRepository.save(user);
    }

    @Test
    void registrationSuccess() throws Exception {
        SignupDto dto = new SignupDto("user2", "user@gmail.com", "password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/signup")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void registrationFailure_UsernameExists() throws Exception {
        SignupDto dto = new SignupDto("user", "user@gmail.com", "password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/signup")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrationFailure_WrongEmailFormat() throws Exception {
        SignupDto dto = new SignupDto("user3", "user@.", "password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/signup")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
