package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.AuthenticatedUser;
import com.pragma.users.domain.model.User;

public interface TokenProviderPort {
    String generateToken(User user);

    AuthenticatedUser decodeToken(String token);
}
