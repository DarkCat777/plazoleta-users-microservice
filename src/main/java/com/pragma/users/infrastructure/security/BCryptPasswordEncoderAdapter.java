package com.pragma.users.infrastructure.security;

import com.pragma.users.domain.spi.EncryptPasswordPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements EncryptPasswordPort {

    private final PasswordEncoder encoder;

    public BCryptPasswordEncoderAdapter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}