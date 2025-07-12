package com.pragma.users.infrastructure.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleEntityMapper extends BaseEntityMapper<Role, JpaRoleEntity> {
}
