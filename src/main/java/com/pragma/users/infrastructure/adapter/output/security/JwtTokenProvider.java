package com.pragma.users.infrastructure.adapter.output.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.TokenProviderPort;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@Log4j2
public class JwtTokenProvider implements TokenProviderPort {

    private final Algorithm hmac512;
    private final JWTVerifier verifier;
    private final Duration jwtTokenValidity;
    private final String issuer;

    public JwtTokenProvider(
            @Value("${spring.application.name}") final String issuer,
            @Value("${jwt.secret}") final String secret,
            @Value("${jwt.expiration-minutes}") final long expirationMinutes
    ) {
        this.issuer = issuer;
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
        this.jwtTokenValidity = Duration.ofMinutes(expirationMinutes);
    }

    @Override
    public String generateToken(final User user) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(jwtTokenValidity))
                .withClaim("id", user.getId())
                .withClaim("roles", List.of("ROLE_" + user.getRole().getName().name()))
                .sign(this.hmac512);
    }

    @Override
    public DecodedJWT validateToken(final String token) {
        try {
            return verifier.verify(token);
        } catch (final JWTVerificationException verificationEx) {
            log.warn("Token invalid: {}", verificationEx.getMessage());
            return null;
        }
    }
}
