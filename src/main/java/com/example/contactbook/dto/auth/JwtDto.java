package com.example.contactbook.dto.auth;

public record JwtDto(String accessToken, String refreshToken, String username) {
}
