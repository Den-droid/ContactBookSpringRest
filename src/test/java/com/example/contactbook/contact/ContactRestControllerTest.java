package com.example.contactbook.contact;

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
public class ContactRestControllerTest {
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

        User userLoggedIn = new User("userLoggedIn", "user2@gmail.com", encoder.encode("password"));
        user.setRoles(roleSet);

        userRepository.save(user);
        userRepository.save(userLoggedIn);
    }

    @Test
    void whenUnauthorized_thenReturnUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/contacts")).andExpect(status().isUnauthorized());
    }
}
