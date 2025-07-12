package com.pragma.users.domain.port.output;

import com.pragma.users.domain.model.AuthenticatedUser;
import com.pragma.users.domain.model.User;

public interface TokenProviderPort {
    String generateToken(User user);

    AuthenticatedUser decodeToken(String token);
}
