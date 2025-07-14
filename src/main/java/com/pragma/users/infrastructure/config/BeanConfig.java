package com.pragma.users.infrastructure.config;

import com.pragma.users.domain.spi.EncryptPasswordPort;
import com.pragma.users.domain.spi.RestaurantClientPort;
import com.pragma.users.domain.spi.persistence.RoleRepositoryPort;
import com.pragma.users.domain.spi.persistence.UserRepositoryPort;
import com.pragma.users.domain.usecase.UserUseCase;
import com.pragma.users.domain.usecase.impl.UserUseCaseImpl;
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
            RestaurantClientPort restaurantClientPort,
            EncryptPasswordPort encryptPasswordPort
    ) {
        return new UserUseCaseImpl(userRepositoryPort, roleRepositoryPort, restaurantClientPort, encryptPasswordPort);
    }

}
