package com.pragma.users.application.service.impl;

import com.pragma.users.application.dto.request.CreateUserCommand;
import com.pragma.users.application.dto.response.RoleResponse;
import com.pragma.users.application.dto.response.UserResponse;
import com.pragma.users.application.mapper.UserDtoMapper;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.usecase.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserCommand command;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        command = new CreateUserCommand("John", "Doe", "12345678", "+123456789",
                LocalDate.of(1990, 1, 1), "john@example.com", "password");

        Role role = new Role(1L, RoleName.CUSTOMER, "Customer role");
        user = new User(null, "John", "Doe", "12345678", "+123456789", LocalDate.of(1990, 1, 1), "john@example.com", "password", 1L, role);

        RoleResponse roleResponse = new RoleResponse(1L, "CUSTOMER", "Customer role");
        userResponse = new UserResponse(1L, "John", "Doe", "+123456789", "12345678", LocalDate.of(1990, 1, 1), "john@example.com", 1L, roleResponse);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        when(userDtoMapper.toDomain(command)).thenReturn(user);
        when(userUseCase.createCustomer(user)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.createCustomer(command);

        assertEquals(userResponse, response);
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        when(userDtoMapper.toDomain(command)).thenReturn(user);
        when(userUseCase.createEmployee(user)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.createEmployee(command);

        assertEquals(userResponse, response);
    }

    @Test
    void shouldCreateOwnerSuccessfully() {
        when(userDtoMapper.toDomain(command)).thenReturn(user);
        when(userUseCase.createOwner(user)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.createOwner(command);

        assertEquals(userResponse, response);
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        Long userId = 1L;
        when(userUseCase.findById(userId)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.findUserById(userId);

        assertEquals(userResponse, response);
    }

    @Test
    void shouldFindUserByEmailSuccessfully() {
        String email = "john@example.com";
        when(userUseCase.findByEmail(email)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.findUserByEmail(email);

        assertEquals(userResponse, response);
    }
}
