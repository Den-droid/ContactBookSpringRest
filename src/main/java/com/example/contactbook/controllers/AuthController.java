package com.example.contactbook.controllers;

import com.example.contactbook.dto.request.LoginRequest;
import com.example.contactbook.dto.request.SignupRequest;
import com.example.contactbook.dto.request.TokenRefreshRequest;
import com.example.contactbook.dto.response.JwtResponse;
import com.example.contactbook.dto.response.MessageResponse;
import com.example.contactbook.dto.response.TokenRefreshResponse;
import com.example.contactbook.exceptions.UsernameAlreadyExistsException;
import com.example.contactbook.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            MessageResponse messageResponse = authenticationService.registerUser(signUpRequest);
            return ResponseEntity.ok(messageResponse);
        } catch (UsernameAlreadyExistsException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage()));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenRefreshResponse messageResponse = authenticationService.refreshToken(tokenRefreshRequest);
        return ResponseEntity.ok(messageResponse);
    }
}
