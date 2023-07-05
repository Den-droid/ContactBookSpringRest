package com.example.contactbook.dto.response;

import java.time.LocalDate;

public record TokenRefreshErrorMessage(int statusCode, LocalDate date, String message) {
}
