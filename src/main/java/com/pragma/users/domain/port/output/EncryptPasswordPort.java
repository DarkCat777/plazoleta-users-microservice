package com.pragma.users.domain.port.output;

public interface EncryptPasswordPort {
    String encode(String rawPassword);
}