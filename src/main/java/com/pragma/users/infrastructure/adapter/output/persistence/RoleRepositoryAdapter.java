package com.pragma.users.infrastructure.adapter.output.persistence;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.port.output.RoleRepositoryPort;
import com.pragma.users.infrastructure.adapter.mapper.RoleEntityMapper;
import com.pragma.users.infrastructure.adapter.output.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final JpaRoleRepository jpaRoleRepository;
    private final RoleEntityMapper roleMapper;

    @Override
    public Optional<Role> findByName(RoleName roleName) {
        return jpaRoleRepository.findByName(roleName.name())
                .map(roleMapper::toDomain);
    }
}
