package com.example.contactbook.dto.auth;

import java.util.List;

public record JwtDto(String token, String refreshToken, String username, List<String> roles){
}
