package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.RoleName;
import com.pragma.users.infrastructure.adapter.input.dto.RoleResponse;
import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = RoleMapperImpl.class)
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void toDomain_shouldMapEntityToDomain() {
        JpaRoleEntity entity = new JpaRoleEntity(1L, "OWNER", "Owner role");

        Role role = roleMapper.toDomain(entity);

        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals(RoleName.OWNER, role.getName());
        assertEquals("Owner role", role.getDescription());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        Role role = new Role(2L, RoleName.ADMINISTRATOR, "Admin role");

        JpaRoleEntity entity = roleMapper.toEntity(role);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("ADMINISTRATOR", entity.getName());
        assertEquals("Admin role", entity.getDescription());
    }

    @Test
    void toResponse_shouldMapDomainToResponse() {
        Role role = new Role(3L, RoleName.CUSTOMER, "Customer role");

        RoleResponse response = roleMapper.toResponse(role);

        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals("CUSTOMER", response.getName());
        assertEquals("Customer role", response.getDescription());
    }
}
