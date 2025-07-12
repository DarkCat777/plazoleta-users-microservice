package com.pragma.users.infrastructure.output.jpa.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.infrastructure.output.jpa.model.JpaRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleEntityMapper {

    Role toDomain(JpaRoleEntity jpaRoleEntity);

}
