package com.pragma.users.infrastructure.adapter.output.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String issuer = "test-app";
        String secret = "my-secret-key";
        long expirationMinutes = 60;
        jwtTokenProvider = new JwtTokenProvider(issuer, secret, expirationMinutes);
    }

    @Test
    void shouldGenerateValidToken() {
        // Given
        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .identityDocument("12345678")
                .phoneNumber("987654321")
                .birthdate(LocalDate.of(2000, 1, 1))
                .email("john@example.com")
                .password("password")
                .role(new Role(1L, RoleName.OWNER, "Owner role"))
                .build();

        // When
        String token = jwtTokenProvider.generateToken(user);

        // Then
        assertNotNull(token);
        DecodedJWT decodedJWT = jwtTokenProvider.validateToken(token);
        assertNotNull(decodedJWT);
        assertEquals("john@example.com", decodedJWT.getSubject());
        assertEquals("test-app", decodedJWT.getIssuer());
        assertTrue(decodedJWT.getClaim("roles").asList(String.class).contains("ROLE_OWNER"));
    }

    @Test
    void shouldReturnNullForInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When
        DecodedJWT result = jwtTokenProvider.validateToken(invalidToken);

        // Then
        assertNull(result);
    }
}
