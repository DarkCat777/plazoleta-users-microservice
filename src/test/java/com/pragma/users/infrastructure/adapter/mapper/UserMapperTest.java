package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.input.dto.UserResponse;
import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import com.pragma.users.infrastructure.adapter.output.model.JpaUserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
        UserMapperImpl.class,
        RoleMapperImpl.class
})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toDomain_shouldMapEntityToDomain() {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(1L);
        entity.setFirstname("Jane");
        entity.setLastname("Doe");
        entity.setIdentityDocument("87654321");
        entity.setPhoneNumber("999999999");
        entity.setBirthdate(LocalDate.of(1990, 1, 1));
        entity.setEmail("jane@example.com");
        entity.setPassword("secret");
        entity.setRole(new JpaRoleEntity(2L, "OWNER", "Owner role"));

        User user = userMapper.toDomain(entity);

        assertNotNull(user);
        assertEquals("Jane", user.getFirstname());
        assertEquals("OWNER", user.getRole().getName().name());
        assertEquals("Owner role", user.getRole().getDescription());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        Role role = new Role(2L, RoleName.OWNER, "Owner role");

        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Smith")
                .identityDocument("12345678")
                .phoneNumber("987654321")
                .birthdate(LocalDate.of(1995, 5, 5))
                .email("john@example.com")
                .password("hashed")
                .role(role)
                .build();

        JpaUserEntity entity = userMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals("John", entity.getFirstname());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("OWNER", entity.getRole().getName());
        assertEquals("Owner role", entity.getRole().getDescription());
    }

    @Test
    void toResponse_shouldMapDomainToResponse() {
        Role role = new Role(3L, RoleName.ADMINISTRATOR, "Admin role");

        User user = User.builder()
                .id(10L)
                .firstname("Luis")
                .lastname("Rodriguez")
                .identityDocument("11111111")
                .phoneNumber("123456789")
                .birthdate(LocalDate.of(1999, 9, 9))
                .email("luis@example.com")
                .password("encrypted")
                .role(role)
                .build();

        UserResponse response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals("Luis", response.getFirstname());
        assertEquals("ADMINISTRATOR", response.getRole().getName());
        assertEquals("Admin role", response.getRole().getDescription());
    }
}

