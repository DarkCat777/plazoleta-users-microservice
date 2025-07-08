package com.pragma.users.infrastructure.config;

import com.pragma.users.application.port.input.CreateOwnerUseCase;
import com.pragma.users.application.port.input.FindUserByIdUseCase;
import com.pragma.users.application.service.CreateOwnerService;
import com.pragma.users.application.service.FindUserByIdService;
import com.pragma.users.domain.port.output.EncryptPasswordPort;
import com.pragma.users.domain.port.output.RoleRepository;
import com.pragma.users.domain.port.output.UserRepository;
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
            UserRepository userRepository,
            RoleRepository roleRepository,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new CreateOwnerService(userRepository, roleRepository, encryptPasswordPort);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepository userRepository) {
        return new FindUserByIdService(userRepository);
    }
}
