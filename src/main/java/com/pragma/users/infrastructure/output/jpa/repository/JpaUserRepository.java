package com.pragma.users.infrastructure.output.jpa.repository;

import com.pragma.users.infrastructure.output.jpa.model.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<JpaUserEntity> findByEmail(String email);
}
