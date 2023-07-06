package com.example.contactbook.authentication.utils;

import com.example.contactbook.security.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenUtilTest {
    private final String accessTokenSecret =
            "=================================ContactBookTestKey====================================";

    @ParameterizedTest
    @NullAndEmptySource
    void cornerCases(String token) {
        JwtUtils jwtUtils = new JwtUtils();
        assertThat(jwtUtils.validateAccessToken(token)).isFalse();
    }

    @Test
    void testExpirationOfToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String token = jwtUtils.generateTokenFromUsername("user", accessTokenSecret, -1);
        assertThat(jwtUtils.validateAccessToken(token)).isFalse();
    }

    @Test
    void testsSafeExtractionOfSubjectFromToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String token = jwtUtils.generateTokenFromUsername("user", accessTokenSecret, 1000000);

        Assertions.assertDoesNotThrow(() -> jwtUtils.getUserNameFromToken(token, accessTokenSecret));

        String userName = jwtUtils.getUserNameFromToken(token, accessTokenSecret);
        assertThat(userName).isEqualTo("user");
    }
}
