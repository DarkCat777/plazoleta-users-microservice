package com.pragma.users.infrastructure.security;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.persistence.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private UserRepositoryPort userRepositoryPort;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        userDetailsService = new UserDetailsServiceImpl(userRepositoryPort);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        String email = "john@example.com";
        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .password("encrypted-password")
                .birthdate(LocalDate.of(2000, 1, 1))
                .phoneNumber("987654321")
                .identityDocument("12345678")
                .role(new Role(1L, RoleName.ADMINISTRATOR, "Admin role"))
                .build();

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encrypted-password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRATOR")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String email = "notfound@example.com";
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(email));

        assertEquals("El usuario con email " + email + " no existe.", exception.getMessage());
    }
}
