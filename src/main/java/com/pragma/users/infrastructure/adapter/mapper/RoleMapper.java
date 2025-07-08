package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.infrastructure.adapter.input.dto.RoleResponse;
import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends BaseMapper<Role, JpaRoleEntity, RoleResponse> {
}
