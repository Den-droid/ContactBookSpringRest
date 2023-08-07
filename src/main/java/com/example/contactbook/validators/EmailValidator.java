package com.example.contactbook.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class EmailValidator {
    @Value("${app.email.validation-api-key}")
    private String validationApiKey;
    private final String baseUrl = "http://apilayer.net/api/check?access_key=";
    private final String regex =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean validateWhole(String email) {
//        RestTemplate restTemplate = new RestTemplate();
//        String requestUrl = baseUrl + validationApiKey + "&email=" + email + "&smtp=0&format=1";
//
//        Map<String, Object> res = restTemplate.getForObject(requestUrl, Map.class);
//
//        if (res != null) {
//            if (res.containsKey("format_valid"))
//                return (boolean) res.getOrDefault("format_valid", false);
//            else if (res.containsKey("success")) {
//                return (boolean) res.getOrDefault("success", false);
//            }
//        }

        return email.matches(regex);
    }
}
