package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.infrastructure.adapter.input.rest.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleResponseMapper extends BaseResponseMapper<Role, RoleResponse> {
}
