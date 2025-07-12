package com.pragma.users.infrastructure.adapter.output.security;

import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.persistence.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + username + " no existe."));

        String roleName = "ROLE_" + user.getRole().getName().name();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(roleName)
                .build();
    }
}
