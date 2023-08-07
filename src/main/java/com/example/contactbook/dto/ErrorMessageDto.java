package com.example.contactbook.dto;

public record ErrorMessageDto(int statusCode, String timestamp, String message) {
}
