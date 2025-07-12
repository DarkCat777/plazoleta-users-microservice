package com.pragma.users.domain.spi;

public interface EncryptPasswordPort {
    String encode(String rawPassword);
}