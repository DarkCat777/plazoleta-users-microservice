package com.pragma.users.domain.spi.persistence;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;

import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> findByName(RoleName roleName);
}