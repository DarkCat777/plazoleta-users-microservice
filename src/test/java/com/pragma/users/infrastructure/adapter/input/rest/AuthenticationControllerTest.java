package com.pragma.users.infrastructure.adapter.input.rest;


import com.pragma.users.application.port.input.FindUserByEmailUseCase;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.TokenProviderPort;
import com.pragma.users.infrastructure.adapter.input.rest.request.AuthenticationRequest;
import com.pragma.users.infrastructure.adapter.input.rest.response.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private FindUserByEmailUseCase findUserByEmailUseCase;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private AuthenticationController controller;

    public AuthenticationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTokenWhenAuthenticationIsSuccessful() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encrypted")
                .role(new Role(1L, RoleName.CUSTOMER, "Cliente"))
                .build();
        String expectedToken = "jwt-token";

        when(findUserByEmailUseCase.getByEmail(request.getUsername())).thenReturn(user);
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
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrong-password");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.authenticate(request);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(findUserByEmailUseCase, tokenProviderPort);
    }
}
