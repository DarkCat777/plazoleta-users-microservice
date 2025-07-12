package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.response.RoleResponse;
import com.pragma.users.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleDtoMapper extends BaseResponseMapper<Role, RoleResponse> {
}
