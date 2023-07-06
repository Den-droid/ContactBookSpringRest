package com.example.contactbook.services.impl;

import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.dto.auth.JwtDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshRequestDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshResponseDto;
import com.example.contactbook.entities.Role;
import com.example.contactbook.entities.User;
import com.example.contactbook.entities.enums.EnumRole;
import com.example.contactbook.exceptions.TokenRefreshException;
import com.example.contactbook.exceptions.UserException;
import com.example.contactbook.mappers.UserMapper;
import com.example.contactbook.repositories.RoleRepository;
import com.example.contactbook.repositories.UserRepository;
import com.example.contactbook.security.jwt.JwtUtils;
import com.example.contactbook.security.user_details.UserDetailsImpl;
import com.example.contactbook.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     UserMapper userMapper,
                                     JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    public JwtDto authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return new JwtDto(accessToken, refreshToken, userDetails.getUsername());
    }

    public MessageResponseDto registerUser(SignupDto signupDto) throws UserException {
        if (userRepository.existsByUsername(signupDto.username())) {
            throw new UserException(signupDto.username(), "User already exists!");
        }

        User user = userMapper.mapUser(signupDto.username(),
                signupDto.email(), signupDto.password());

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponseDto("User registered successfully!");
    }

    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto)
            throws TokenRefreshException {
        String requestRefreshToken = tokenRefreshRequestDto.refreshToken();

        String username = jwtUtils.getUserNameFromRefreshToken(requestRefreshToken);
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }

        if (!jwtUtils.validateRefreshToken(requestRefreshToken)) {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is invalid! " +
                    "Please sign in again to get new!");
        }

        User user = optionalUser.get();

        String accessToken = jwtUtils.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenRefreshResponseDto(accessToken, refreshToken);
    }
}
