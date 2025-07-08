package com.pragma.users.domain.port.output;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragma.users.domain.model.User;

public interface TokenProviderPort {
    String generateToken(User user);

    DecodedJWT validateToken(String token);
}
