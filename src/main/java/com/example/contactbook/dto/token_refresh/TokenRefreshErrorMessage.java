package com.example.contactbook.dto.token_refresh;

import java.time.LocalDate;

public record TokenRefreshErrorMessage(int statusCode, LocalDate date, String message) {
}
