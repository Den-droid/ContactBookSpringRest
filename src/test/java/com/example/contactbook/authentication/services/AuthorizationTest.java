package com.example.contactbook.authentication.services;

import com.example.contactbook.dto.auth.JwtDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.entities.Role;
import com.example.contactbook.entities.User;
import com.example.contactbook.entities.enums.EnumRole;
import com.example.contactbook.repositories.RoleRepository;
import com.example.contactbook.repositories.UserRepository;
import com.example.contactbook.security.jwt.JwtUtils;
import com.example.contactbook.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorizationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtils jwtUtils;

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
    void gettingContactsFailure_NotAuthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/contacts")).andExpect(status().isUnauthorized());
    }

    @Test
    void gettingContactsFailure_WrongAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/contacts")
                        .header("Authorization", "Bearer dgnusnfgsn0gupidfgdfg"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void gettingContactsFailure_AccessTokenExpired() throws Exception {
        String expiredToken = jwtUtils.generateAccessToken("user", -1);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/contacts")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void gettingContactsSuccess() throws Exception {
        LoginDto loginDto = new LoginDto("user", "password");
        JwtDto jwtDto = authenticationService.authenticateUser(loginDto);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/contacts")
                        .header("Authorization", "Bearer " + jwtDto.accessToken()))
                .andExpect(status().isOk());
    }
}
