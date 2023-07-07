package com.example.contactbook.services;

import com.example.contactbook.dto.MessageResponseDto;
import com.example.contactbook.dto.auth.JwtDto;
import com.example.contactbook.dto.auth.LoginDto;
import com.example.contactbook.dto.auth.SignupDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshRequestDto;
import com.example.contactbook.dto.token_refresh.TokenRefreshResponseDto;

public interface AuthenticationService {
    JwtDto authenticateUser(LoginDto loginDto);

    MessageResponseDto registerUser(SignupDto signupDto);

    TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto);
}
