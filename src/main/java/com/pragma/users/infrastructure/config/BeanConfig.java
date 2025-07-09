package com.pragma.users.infrastructure.config;

import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.application.port.input.FindUserByIdUseCase;
import com.pragma.users.application.service.CreateOwnerUseCaseImpl;
import com.pragma.users.application.service.FindUserByIdUseCaseImpl;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepositoryPort;
import com.pragma.users.domain.port.output.UserRepositoryPort;
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
    public CreateOwnerUseCase createOwnerUseCase(
            UserRepositoryPort userRepositoryPort,
            RoleRepositoryPort roleRepositoryPort,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new CreateOwnerUseCaseImpl(userRepositoryPort, roleRepositoryPort, encryptPasswordPort);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepositoryPort userRepositoryPort) {
        return new FindUserByIdUseCaseImpl(userRepositoryPort);
    }
}
