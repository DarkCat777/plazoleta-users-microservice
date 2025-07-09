package com.pragma.users.application.service;

import com.pragma.users.application.exception.UserNotFoundException;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindUserByEmailUseCaseImplTest {

    private UserRepositoryPort userRepositoryPort;
    private FindUserByEmailUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        useCase = new FindUserByEmailUseCaseImpl(userRepositoryPort);
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        // Given
        String email = "test@example.com";
        User user = User.builder().id(1L).email(email).build();

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        User result = useCase.getByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepositoryPort).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String email = "notfound@example.com";
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> useCase.getByEmail(email));
        assertEquals("Usuario no encontrado con el email: " + email, exception.getMessage());
        verify(userRepositoryPort).findByEmail(email);
    }
}
