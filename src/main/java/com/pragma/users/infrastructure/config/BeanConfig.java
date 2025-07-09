package com.pragma.users.infrastructure.config;

import com.pragma.users.application.port.input.*;
import com.pragma.users.application.service.*;
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
    public CreateEmployeeUseCase createEmployeeUseCase(
            UserRepositoryPort userRepositoryPort,
            RoleRepositoryPort roleRepositoryPort,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new CreateEmployeeUseCaseImpl(userRepositoryPort, roleRepositoryPort, encryptPasswordPort);
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(
            UserRepositoryPort userRepositoryPort,
            RoleRepositoryPort roleRepositoryPort,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new CreateCustomerUseCaseImpl(userRepositoryPort, roleRepositoryPort, encryptPasswordPort);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepositoryPort userRepositoryPort) {
        return new FindUserByIdUseCaseImpl(userRepositoryPort);
    }

    @Bean
    public FindUserByEmailUseCase findUserByEmailUseCase(UserRepositoryPort userRepositoryPort) {
        return new FindUserByEmailUseCaseImpl(userRepositoryPort);
    }
}
