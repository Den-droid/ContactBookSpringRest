package com.example.contactbook.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PhoneNumberValidator {
    @Value("${app.phone-number.validation-api-key}")
    private String validationApiKey;
    private final String baseUrl = "http://apilayer.net/api/validate?access_key=";
    private final String regex =
            "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

    public boolean validateWhole(String number) {
        boolean validFormat = number.matches(regex);

        if (validFormat) {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = baseUrl + validationApiKey +
                    "&number=" + number + "&country_code=&format=1";

            Map<String, Object> res = restTemplate.getForObject(requestUrl, Map.class);

            if (res != null && res.containsKey("valid")) {
                return (boolean) res.getOrDefault("valid", false);
            }
        }

        return validFormat;
    }
}
