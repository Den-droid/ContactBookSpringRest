package com.example.contactbook.dto.response;

import java.util.List;

public record JwtResponse(String token, String refreshToken, String username, List<String> roles){
}
