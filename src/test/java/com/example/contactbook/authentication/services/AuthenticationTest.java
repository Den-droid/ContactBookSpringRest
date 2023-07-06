package com.example.contactbook.authentication.services;

import com.example.contactbook.dto.auth.LoginDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationTest {
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
    void loginSuccess() throws Exception {
        LoginDto dto = new LoginDto("user", "password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/signin")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void loginFailure_UsernameWrong() throws Exception {
        LoginDto dto = new LoginDto("wrongStudentName", "some_password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/signin")
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginFailure_LoginOk_But_PasswordWrong() throws Exception {
        LoginDto dto = new LoginDto("user", "some_wrong_password");

        String jsonBody = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/api/auth/login")
                        .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
