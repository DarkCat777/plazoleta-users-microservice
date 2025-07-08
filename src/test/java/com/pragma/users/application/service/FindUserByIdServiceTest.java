package com.pragma.users.application.service;

import com.pragma.users.application.exception.UserNotFoundException;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdService findUserByIdService;

    @Test
    void shouldReturnUserWhenExists() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).email("john@example.com").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = findUserByIdService.getById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Given
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> findUserByIdService.getById(userId));

        assertEquals("Usuario no encontrado con el id: 99", exception.getMessage());
        verify(userRepository).findById(userId);
    }
}
