package com.pragma.users.application.service;

import com.pragma.users.application.dto.CreateEmployeeCommand;
import com.pragma.users.application.exception.RoleNotFoundException;
import com.pragma.users.application.exception.UserAlreadyExistsException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepositoryPort;
import com.pragma.users.domain.port.output.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEmployeeUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private RoleRepositoryPort roleRepositoryPort;
    @Mock
    private EncryptPasswordPort passwordEncoder;

    @InjectMocks
    private CreateEmployeeUseCaseImpl createEmployeeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        // Arrange
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                "John", "Doe", "12345678", "987654321",
                LocalDate.of(1995, 1, 1), "john.doe@example.com", "password123"
        );

        Role employeeRole = new Role(3L, RoleName.EMPLOYEE, "Empleado");

        when(userRepositoryPort.existsByEmail(command.getEmail())).thenReturn(false);
        when(roleRepositoryPort.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(passwordEncoder.encode(command.getPassword())).thenReturn("hashedPassword");

        User expectedUser = User.builder()
                .firstname("John")
                .lastname("Doe")
                .identityDocument("12345678")
                .phoneNumber("987654321")
                .birthdate(LocalDate.of(1995, 1, 1))
                .email("john.doe@example.com")
                .password("hashedPassword")
                .role(employeeRole)
                .build();

        when(userRepositoryPort.save(any(User.class))).thenReturn(expectedUser);

        // Act
        User result = createEmployeeUseCase.createEmployee(command);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals(RoleName.EMPLOYEE, result.getRole().getName());

        verify(userRepositoryPort).existsByEmail(command.getEmail());
        verify(roleRepositoryPort).findByName(RoleName.EMPLOYEE);
        verify(passwordEncoder).encode(command.getPassword());
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfEmailExists() {
        // Arrange
        String email = "existing@example.com";
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                "Jane", "Doe", "87654321", "999888777",
                LocalDate.of(1990, 5, 5), email, "password"
        );

        when(userRepositoryPort.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> createEmployeeUseCase.createEmployee(command));

        verify(userRepositoryPort).existsByEmail(email);
        verifyNoMoreInteractions(roleRepositoryPort, passwordEncoder, userRepositoryPort);
    }

    @Test
    void shouldThrowExceptionIfRoleNotFound() {
        // Arrange
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                "Luis", "Smith", "10101010", "321321321",
                LocalDate.of(1992, 3, 3), "luis@example.com", "securePass"
        );

        when(userRepositoryPort.existsByEmail(command.getEmail())).thenReturn(false);
        when(roleRepositoryPort.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> createEmployeeUseCase.createEmployee(command));

        verify(userRepositoryPort).existsByEmail(command.getEmail());
        verify(roleRepositoryPort).findByName(RoleName.EMPLOYEE);
        verifyNoMoreInteractions(passwordEncoder, userRepositoryPort);
    }
}
