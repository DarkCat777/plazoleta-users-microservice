package com.pragma.users.infrastructure.adapter.input.security;

import com.pragma.users.domain.model.AuthenticatedUser;
import com.pragma.users.domain.port.output.TokenProviderPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationQueryFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenProviderPort tokenProvider;

    @InjectMocks
    private JwtAuthenticationRequestFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotAuthenticateWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenProvider);
    }

    @Test
    void shouldAuthenticateWhenValidTokenAndUser() throws Exception {
        String token = "valid.jwt.token";
        String email = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.getEmail()).thenReturn(email);
        when(tokenProvider.decodeToken(token)).thenReturn(authenticatedUser);

        UserDetails userDetails = new User(email, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void shouldNotAuthenticateWhenTokenInvalid() throws Exception {
        String token = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenProvider.decodeToken(token)).thenReturn(null); // simulate invalid token

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
