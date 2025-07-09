package com.pragma.users.application.service;

import com.pragma.users.application.dto.CreateCustomerCommand;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCustomerUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RoleRepositoryPort roleRepositoryPort;

    @Mock
    private EncryptPasswordPort passwordEncoder;

    @InjectMocks
    private CreateCustomerUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        // Given
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Luis", "Gomez", "98765432", "912345678",
                LocalDate.of(1995, 5, 10), "luis@mail.com", "1234"
        );

        Role role = new Role(4L, RoleName.CUSTOMER, "Cliente del sistema");
        User expectedUser = User.builder()
                .firstname("Luis")
                .lastname("Gomez")
                .identityDocument("98765432")
                .phoneNumber("912345678")
                .birthdate(LocalDate.of(1995, 5, 10))
                .email("luis@mail.com")
                .password("hashed1234")
                .role(role)
                .build();

        when(userRepositoryPort.existsByEmail("luis@mail.com")).thenReturn(false);
        when(roleRepositoryPort.findByName(RoleName.CUSTOMER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("hashed1234");
        when(userRepositoryPort.save(any(User.class))).thenReturn(expectedUser);

        // When
        User result = useCase.createCustomer(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("luis@mail.com");
        assertThat(result.getPassword()).isEqualTo("hashed1234");
        assertThat(result.getRole().getName()).isEqualTo(RoleName.CUSTOMER);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        // Given
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Ana", "Perez", "87654321", "900111222",
                LocalDate.of(1998, 3, 20), "ana@mail.com", "pass"
        );

        when(userRepositoryPort.existsByEmail("ana@mail.com")).thenReturn(true);

        // Then
        assertThatThrownBy(() -> useCase.createCustomer(command))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("ana@mail.com");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenCustomerRoleNotFound() {
        // Given
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Carlos", "Diaz", "12312312", "955000000",
                LocalDate.of(1990, 1, 1), "carlos@mail.com", "password"
        );

        when(userRepositoryPort.existsByEmail("carlos@mail.com")).thenReturn(false);
        when(roleRepositoryPort.findByName(RoleName.CUSTOMER)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> useCase.createCustomer(command))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("CUSTOMER");

        verify(userRepositoryPort, never()).save(any());
    }
}
