package com.example.contactbook.services;

import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.dto.auth.RefreshTokenDto;
import com.example.contactbook.dto.auth.TokensDto;
import com.example.contactbook.entities.User;

public interface AuthenticationService {
    TokensDto authenticateUser(LoginDto loginDto);

    void registerUser(SignupDto signupDto);

    TokensDto refreshToken(RefreshTokenDto refreshTokenDto);
}
