package com.pragma.users.infrastructure.security;

import com.pragma.users.domain.model.AuthenticatedUser;
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
        AuthenticatedUser authenticatedUser = jwtTokenProvider.decodeToken(token);
        assertNotNull(authenticatedUser);
        assertEquals("john@example.com", authenticatedUser.getEmail());
        assertTrue(authenticatedUser.getRoles().contains("ROLE_OWNER"));
    }

    @Test
    void shouldReturnNullForInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When
        AuthenticatedUser authenticatedUser = jwtTokenProvider.decodeToken(invalidToken);

        // Then
        assertNull(authenticatedUser);
    }
}
