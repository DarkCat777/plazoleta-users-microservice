package com.pragma.users.domain.usecase.impl;

import com.pragma.users.domain.exception.RoleNotFoundException;
import com.pragma.users.domain.exception.UserAlreadyExistsException;
import com.pragma.users.domain.exception.UserNotFoundException;
import com.pragma.users.domain.model.Restaurant;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.EncryptPasswordPort;
import com.pragma.users.domain.spi.RestaurantClientPort;
import com.pragma.users.domain.spi.persistence.RoleRepositoryPort;
import com.pragma.users.domain.spi.persistence.UserRepositoryPort;
import com.pragma.users.domain.validation.errors.WithField;
import com.pragma.users.domain.validation.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private RoleRepositoryPort roleRepository;

    @Mock
    private RestaurantClientPort restaurantClient;

    @Mock
    private EncryptPasswordPort encryptPassword;

    @InjectMocks
    private UserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    private User getValidUser() {
        return User.builder()
                .firstname("John")
                .lastname("Doe")
                .identityDocument("123456789")
                .phoneNumber("+123456789")
                .birthdate(LocalDate.of(2000, 1, 1))
                .email("john@example.com")
                .password("password")
                .build();
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        User user = getValidUser();
        Role role = new Role(1L, RoleName.CUSTOMER, "Cliente");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.CUSTOMER)).thenReturn(Optional.of(role));
        when(encryptPassword.encode(user.getPassword())).thenReturn("encrypted");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.createCustomer(user);

        assertEquals(role, result.getRole());
        assertEquals("encrypted", result.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenCustomerEmailExists() {
        User user = getValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> useCase.createCustomer(user));
    }

    @Test
    void shouldThrowWhenCustomerRoleNotFound() {
        User user = getValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.CUSTOMER)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> useCase.createCustomer(user));
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        User user = getValidUser();
        Role role = new Role(2L, RoleName.EMPLOYEE, "Empleado");
        Restaurant restaurant = new Restaurant(1L, "Pragma", "Dirección", "9845698546", "logoUrl", "56945869", 1L);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.of(role));
        when(restaurantClient.findByOwner()).thenReturn(Optional.of(restaurant));
        when(encryptPassword.encode(user.getPassword())).thenReturn("encrypted");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.createEmployee(user);

        assertEquals(role, result.getRole());
        assertEquals("encrypted", result.getPassword());
    }

    @Test
    void shouldThrowWhenEmployeeRoleNotFound() {
        User user = getValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> useCase.createEmployee(user));
    }

    @Test
    void shouldCreateOwnerSuccessfully() {
        User user = getValidUser();
        Role role = new Role(3L, RoleName.OWNER, "Propietario");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.OWNER)).thenReturn(Optional.of(role));
        when(encryptPassword.encode(user.getPassword())).thenReturn("encrypted");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.createOwner(user);

        assertEquals(role, result.getRole());
        assertEquals("encrypted", result.getPassword());
    }

    @Test
    void shouldThrowWhenOwnerEmailExists() {
        User user = getValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> useCase.createOwner(user));
    }

    @Test
    void shouldThrowWhenOwnerRoleNotFound() {
        User user = getValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RoleName.OWNER)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> useCase.createOwner(user));
    }

    @Test
    void shouldFindUserByEmail() {
        User user = getValidUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = useCase.findByEmail(user.getEmail());

        assertEquals(user, result);
    }

    @Test
    void shouldThrowWhenUserEmailNotFound() {
        when(userRepository.findByEmail("not@found.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.findByEmail("not@found.com"));
    }

    @Test
    void shouldFindUserById() {
        User user = getValidUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = useCase.findById(1L);

        assertEquals(user, result);
    }

    @Test
    void shouldThrowWhenUserIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.findById(999L));
    }

    @Test
    void shouldThrowValidationExceptionWhenFirstnameIsBlank() {
        User user = new User(null, "", "Doe", "12345678", "+123456789", LocalDate.of(1990, 1, 1), "john@example.com", "pass", 1L, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.createCustomer(user));

        assertTrue(exception.getErrors().stream().map(e -> (WithField) e).anyMatch(e -> e.getField().equals("firstname")));
    }

    @Test
    void shouldThrowValidationExceptionWhenPhoneNumberIsInvalid() {
        User user = new User(null, "John", "Doe", "12345678", "abc123", LocalDate.of(1990, 1, 1), "john@example.com", "pass", 1L, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.createCustomer(user));

        assertTrue(exception.getErrors().stream().map(e -> (WithField) e).anyMatch(e -> e.getField().equals("phoneNumber")));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsInvalid() {
        User user = new User(null, "John", "Doe", "12345678", "+123456789", LocalDate.of(1990, 1, 1), "invalid-email", "pass", 1L, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.createCustomer(user));

        assertTrue(exception.getErrors().stream().map(e -> (WithField) e).anyMatch(e -> e.getField().equals("email")));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdateIsMissing() {
        User user = new User(null, "John", "Doe", "12345678", "+123456789", null, "john@example.com", "pass", 1L, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.createCustomer(user));

        assertTrue(exception.getErrors().stream().map(e -> (WithField) e).anyMatch(e -> e.getField().equals("birthdate")));
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdateIsTooRecentForOwner() {
        User user = new User(null, "John", "Doe", "12345678", "+123456789", LocalDate.now().minusYears(17), "john@example.com", "pass", 1L, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.createOwner(user));

        assertTrue(exception.getErrors().stream().map(e -> (WithField) e).anyMatch(e -> e.getField().equals("birthdate")));
    }
}
