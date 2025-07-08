package com.pragma.users.infrastructure.adapter.output.repository;

import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRoleRepository extends JpaRepository<JpaRoleEntity, Long> {
    Optional<JpaRoleEntity> findByName(String name);
}
