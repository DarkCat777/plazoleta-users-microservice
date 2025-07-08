package com.pragma.users.infrastructure.adapter.output.persistence;

import com.pragma.users.domain.model.User;
import com.pragma.users.domain.port.output.UserRepository;
import com.pragma.users.infrastructure.adapter.mapper.UserMapper;
import com.pragma.users.infrastructure.adapter.output.model.JpaUserEntity;
import com.pragma.users.infrastructure.adapter.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        JpaUserEntity userEntity = jpaUserRepository.save(userMapper.toEntity(user));
        return userMapper.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
