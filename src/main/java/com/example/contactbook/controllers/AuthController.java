package com.example.contactbook.controllers;

import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.dto.auth.JwtDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshRequestDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshResponseDto;
import com.example.contactbook.exceptions.UserException;
import com.example.contactbook.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        JwtDto jwtDto = authenticationService.authenticateUser(loginDto);
        return ResponseEntity.ok(jwtDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpDto) {
        try {
            MessageResponseDto messageResponseDto = authenticationService.registerUser(signUpDto);
            return ResponseEntity.ok(messageResponseDto);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(exception.getMessage()));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        try {
            TokenRefreshResponseDto tokenRefreshResponseDto =
                    authenticationService.refreshToken(tokenRefreshRequestDto);
            return ResponseEntity.ok(tokenRefreshResponseDto);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }
}
