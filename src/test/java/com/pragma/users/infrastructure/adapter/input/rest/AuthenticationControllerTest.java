package com.pragma.users.infrastructure.adapter.input.rest;


import com.pragma.users.application.dto.request.AuthenticationQuery;
import com.pragma.users.application.dto.response.AuthenticationResponse;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.input.usecase.UserUseCase;
import com.pragma.users.domain.port.output.TokenProviderPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserUseCase useCase;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private AuthenticationController controller;

    public AuthenticationControllerTest() {
        openMocks(this);
    }

    @Test
    void shouldReturnTokenWhenAuthenticationIsSuccessful() {
        // Arrange
        AuthenticationQuery request = new AuthenticationQuery("user@example.com", "password");
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encrypted")
                .role(new Role(1L, RoleName.CUSTOMER, "Cliente"))
                .build();
        String expectedToken = "jwt-token";

        when(useCase.findByEmail(request.username())).thenReturn(user);
        when(tokenProviderPort.generateToken(user)).thenReturn(expectedToken);

        // Act
        AuthenticationResponse response = controller.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldThrowUnauthorizedWhenCredentialsAreInvalid() {
        // Arrange
        AuthenticationQuery request = new AuthenticationQuery("user@example.com", "wrong-password");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.authenticate(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(useCase, tokenProviderPort);
    }
}
