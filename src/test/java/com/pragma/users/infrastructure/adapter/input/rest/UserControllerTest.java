package com.pragma.users.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.users.application.dto.CreateCustomerCommand;
import com.pragma.users.application.dto.CreateEmployeeCommand;
import com.pragma.users.application.dto.CreateOwnerCommand;
import com.pragma.users.application.exception.RoleNotFoundException;
import com.pragma.users.application.exception.UnderageUserException;
import com.pragma.users.application.exception.UserAlreadyExistsException;
import com.pragma.users.application.exception.UserNotFoundException;
import com.pragma.users.application.port.input.CreateCustomerUseCase;
import com.pragma.users.application.port.input.CreateEmployeeUseCase;
import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.application.port.input.FindUserByIdUseCase;
import com.pragma.users.config.TestSecurityConfig;
import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.input.rest.response.RoleResponse;
import com.pragma.users.infrastructure.adapter.input.rest.response.UserResponse;
import com.pragma.users.infrastructure.adapter.input.rest.handler.GlobalExceptionHandler;
import com.pragma.users.infrastructure.adapter.input.security.JwtAuthenticationRequestFilter;
import com.pragma.users.infrastructure.adapter.mapper.UserResponseMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationRequestFilter.class)
})
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateOwnerUseCase createOwnerUseCase;

    @MockitoBean
    private CreateEmployeeUseCase createEmployeeUseCase;

    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockitoBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockitoBean
    private UserResponseMapper userMapper;

    private final String BASE_URL = "/api/v1/users";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldCreateOwnerSuccessfully() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand("John", "Doe", "12345678", "987654321",
                LocalDate.of(2000, 1, 1), "john@example.com", "password");

        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .phoneNumber("987654321")
                .build();

        RoleResponse roleResponse = new RoleResponse(2L, "OWNER", "Owner role");
        UserResponse userResponse = new UserResponse(1L, "John", "Doe", "john@example.com", "987654321", "73108217", LocalDate.now(), roleResponse);

        when(createOwnerUseCase.createOwner(command)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // When / Then
        mockMvc.perform(post(BASE_URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.role.name").value("OWNER"))
                .andExpect(jsonPath("$.role.description").value("Owner role"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldReturn409WhenOwnerAlreadyExists() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand("John", "Doe", "12345678", "987654321",
                LocalDate.of(2000, 1, 1), "john@example.com", "password");

        when(createOwnerUseCase.createOwner(command))
                .thenThrow(new UserAlreadyExistsException("john@example.com"));

        // When / Then
        mockMvc.perform(post(BASE_URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Usuario ya existe"))
                .andExpect(jsonPath("$.message").value("Ya existe un usuario registrado con el correo: " + command.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldReturn400WhenValidationFails() throws Exception {
        // firstname vacío y email inválido
        String invalidJson = """
                {
                  "firstname": "",
                  "lastname": "Doe",
                  "identityDocument": "12345678",
                  "phoneNumber": "987654321",
                  "birthdate": "2000-01-01",
                  "email": "correo-no-valido",
                  "password": "password"
                }
                """;

        mockMvc.perform(post(BASE_URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Datos inválidos"))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("firstname")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("email")));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldReturn400WhenUserIsUnderage() throws Exception {
        CreateOwnerCommand command = new CreateOwnerCommand(
                "Young", "User", "12345678", "987654321",
                LocalDate.now().minusYears(16), // menor de edad
                "young@example.com", "password"
        );

        when(createOwnerUseCase.createOwner(command)).thenThrow(new UnderageUserException());

        mockMvc.perform(post(BASE_URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Usuario menor de edad"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldReturn404WhenRoleNotFound() throws Exception {
        CreateOwnerCommand command = new CreateOwnerCommand(
                "John", "Doe", "12345678", "987654321",
                LocalDate.of(2000, 1, 1),
                "john@example.com", "password"
        );

        when(createOwnerUseCase.createOwner(command))
                .thenThrow(new RoleNotFoundException("OWNER"));

        mockMvc.perform(post(BASE_URL + "/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Rol no encontrado"))
                .andExpect(jsonPath("$.message").value("No existe el rol: OWNER"));
    }

    @Test
    @WithMockUser
    void shouldReturnUserByIdSuccessfully() throws Exception {
        // Given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .firstname("Jane")
                .lastname("Doe")
                .email("jane@example.com")
                .phoneNumber("123456789")
                .build();

        RoleResponse roleResponse = new RoleResponse(1L, "ADMINISTRATOR", "Admin role");
        UserResponse response = new UserResponse(userId, "Jane", "Doe", "jane@example.com", "123456789", "73108217", LocalDate.now(), roleResponse);

        when(findUserByIdUseCase.getById(userId)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        // When / Then
        mockMvc.perform(get(BASE_URL + "/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.role.name").value("ADMINISTRATOR"));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUserNotFound() throws Exception {
        // Given
        Long userId = 99L;
        when(findUserByIdUseCase.getById(userId)).thenThrow(new UserNotFoundException(userId));

        // When / Then
        mockMvc.perform(get(BASE_URL + "/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado"))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado con el id: " + userId));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldCreateEmployeeSuccessfully() throws Exception {
        // Given
        CreateEmployeeCommand command = new CreateEmployeeCommand("Jane", "Smith", "87654321", "123456789",
                LocalDate.of(1995, 5, 15), "jane@example.com", "securePass");

        User user = User.builder()
                .id(2L)
                .firstname("Jane")
                .lastname("Smith")
                .email("jane@example.com")
                .phoneNumber("123456789")
                .build();

        RoleResponse roleResponse = new RoleResponse(3L, "EMPLOYEE", "Empleado del sistema");
        UserResponse userResponse = new UserResponse(2L, "Jane", "Smith", "jane@example.com", "123456789", "87654321", LocalDate.of(1995, 5, 15), roleResponse);

        when(createEmployeeUseCase.createEmployee(command)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // When / Then
        mockMvc.perform(post(BASE_URL + "/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstname").value("Jane"))
                .andExpect(jsonPath("$.role.name").value("EMPLOYEE"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldReturn409WhenEmployeeAlreadyExists() throws Exception {
        CreateEmployeeCommand command = new CreateEmployeeCommand("Jane", "Smith", "87654321", "123456789",
                LocalDate.of(1995, 5, 15), "jane@example.com", "securePass");

        when(createEmployeeUseCase.createEmployee(command))
                .thenThrow(new UserAlreadyExistsException(command.getEmail()));

        mockMvc.perform(post(BASE_URL + "/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Usuario ya existe"))
                .andExpect(jsonPath("$.message").value("Ya existe un usuario registrado con el correo: " + command.getEmail()));

    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldCreateCustomerSuccessfully() throws Exception {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Carlos", "Perez", "76543210", "987654321",
                LocalDate.of(1995, 2, 20), "carlos@example.com", "secure123"
        );

        User user = User.builder()
                .id(5L)
                .firstname("Carlos")
                .lastname("Perez")
                .email("carlos@example.com")
                .phoneNumber("987654321")
                .identityDocument("76543210")
                .birthdate(LocalDate.of(1995, 2, 20))
                .build();

        RoleResponse roleResponse = new RoleResponse(4L, "CUSTOMER", "Cliente del sistema");
        UserResponse userResponse = new UserResponse(
                5L, "Carlos", "Perez", "carlos@example.com",
                "987654321", "76543210", LocalDate.of(1995, 2, 20), roleResponse
        );

        when(createCustomerUseCase.createCustomer(command)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post(BASE_URL + "/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.firstname").value("Carlos"))
                .andExpect(jsonPath("$.role.name").value("CUSTOMER"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldReturn409WhenCustomerAlreadyExists() throws Exception {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Maria", "Lopez", "99999999", "900000000",
                LocalDate.of(1990, 1, 1), "maria@example.com", "pass123"
        );

        when(createCustomerUseCase.createCustomer(command))
                .thenThrow(new UserAlreadyExistsException("maria@example.com"));

        mockMvc.perform(post(BASE_URL + "/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Usuario ya existe"))
                .andExpect(jsonPath("$.message").value("Ya existe un usuario registrado con el correo: maria@example.com"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldReturn404WhenCustomerRoleNotFound() throws Exception {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Esteban", "Rojas", "88888888", "922222222",
                LocalDate.of(1992, 8, 15), "esteban@example.com", "mypassword"
        );

        when(createCustomerUseCase.createCustomer(command))
                .thenThrow(new RoleNotFoundException("CUSTOMER"));

        mockMvc.perform(post(BASE_URL + "/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Rol no encontrado"))
                .andExpect(jsonPath("$.message").value("No existe el rol: CUSTOMER"));
    }

}
