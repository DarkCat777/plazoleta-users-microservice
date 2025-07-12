package com.pragma.users.infrastructure.adapter.input.rest;

import com.pragma.users.application.dto.request.AuthenticationQuery;
import com.pragma.users.application.dto.response.AuthenticationResponse;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.input.usecase.UserUseCase;
import com.pragma.users.domain.port.output.TokenProviderPort;
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
    private final UserUseCase useCase;
    private final TokenProviderPort tokenProviderPort;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody @Validated final AuthenticationQuery authenticationQuery) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationQuery.username(), authenticationQuery.password())
            );
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final User user = useCase.findByEmail(authenticationQuery.username());
        return new AuthenticationResponse(tokenProviderPort.generateToken(user));
    }

}
