package com.pragma.users.infrastructure.adapter.input.rest;

import com.pragma.users.application.port.input.FindUserByEmailUseCase;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.TokenProviderPort;
import com.pragma.users.infrastructure.adapter.input.rest.request.AuthenticationRequest;
import com.pragma.users.infrastructure.adapter.input.rest.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final TokenProviderPort tokenProviderPort;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody @Validated final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final User user = findUserByEmailUseCase.getByEmail(authenticationRequest.getUsername());
        return new AuthenticationResponse(tokenProviderPort.generateToken(user));
    }

}
