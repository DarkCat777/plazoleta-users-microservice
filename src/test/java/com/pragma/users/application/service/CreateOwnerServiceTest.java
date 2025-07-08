package com.pragma.users.application.service;

import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.application.exception.UnderageUserException;
import com.pragma.users.application.exception.UserAlreadyExistsException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepository;
import com.pragma.users.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOwnerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EncryptPasswordPort passwordEncoder;

    @InjectMocks
    private CreateOwnerService createOwnerService;

    private CreateOwnerCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = new CreateOwnerCommand(
                "John",
                "Doe",
                "12345678",
                "987654321",
                LocalDate.now().minusYears(25),
                "john@example.com",
                "plainPassword"
        );
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(validCommand.getEmail())).thenReturn(true);

        // When / Then
        assertThrows(UserAlreadyExistsException.class, () -> createOwnerService.createOwner(validCommand));
        verify(userRepository).existsByEmail(validCommand.getEmail());
        verifyNoMoreInteractions(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void shouldThrowExceptionWhenUserIsUnderage() {
        // Given
        validCommand.setBirthdate(LocalDate.now().minusYears(17)); // menor de edad
        when(userRepository.existsByEmail(validCommand.getEmail())).thenReturn(false);

        // When / Then
        assertThrows(UnderageUserException.class, () -> createOwnerService.createOwner(validCommand));
        verify(userRepository).existsByEmail(validCommand.getEmail());
        verifyNoMoreInteractions(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void shouldCreateOwnerSuccessfully() {
        // Given
        when(userRepository.existsByEmail(validCommand.getEmail())).thenReturn(false);

        Role ownerRole = Role.builder().id(1L).name(RoleName.OWNER).build();
        when(roleRepository.findByName(RoleName.OWNER)).thenReturn(Optional.of(ownerRole));

        when(passwordEncoder.encode(validCommand.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .firstname(validCommand.getFirstname())
                .lastname(validCommand.getLastname())
                .identityDocument(validCommand.getIdentityDocument())
                .phoneNumber(validCommand.getPhoneNumber())
                .birthdate(validCommand.getBirthdate())
                .email(validCommand.getEmail())
                .password("encodedPassword")
                .role(ownerRole)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = createOwnerService.createOwner(validCommand);

        // Then
        assertNotNull(result);
        assertEquals(validCommand.getEmail(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(ownerRole, result.getRole());

        Mockito.verify(userRepository).existsByEmail(validCommand.getEmail());
        Mockito.verify(roleRepository).findByName(RoleName.OWNER);
        Mockito.verify(passwordEncoder).encode(validCommand.getPassword());
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }
}