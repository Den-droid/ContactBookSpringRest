package com.example.contactbook.dto;

import java.util.List;

public record ContactDto(String contactName, List<String> emails, List<String> phoneNumbers) {
}
