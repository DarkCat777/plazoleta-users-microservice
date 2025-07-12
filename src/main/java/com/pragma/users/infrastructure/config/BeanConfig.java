package com.pragma.users.infrastructure.config;

import com.pragma.users.domain.port.input.usecase.UserUseCase;
import com.pragma.users.domain.port.input.usecase.impl.UserUseCaseImpl;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.persistence.RoleRepositoryPort;
import com.pragma.users.domain.port.output.persistence.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserUseCase createUserUseCase(
            UserRepositoryPort userRepositoryPort,
            RoleRepositoryPort roleRepositoryPort,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new UserUseCaseImpl(userRepositoryPort, roleRepositoryPort, encryptPasswordPort);
    }

}
