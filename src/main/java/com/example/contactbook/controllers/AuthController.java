package com.example.contactbook.controllers;

import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.dto.auth.RefreshTokenDto;
import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.dto.auth.TokensDto;
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
        TokensDto jwtDto = authenticationService.authenticateUser(loginDto);
        return ResponseEntity.ok(jwtDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpDto) {
        authenticationService.registerUser(signUpDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        TokensDto tokensDto =
                authenticationService.refreshToken(refreshTokenDto);
        return ResponseEntity.ok(tokensDto);
    }
}
