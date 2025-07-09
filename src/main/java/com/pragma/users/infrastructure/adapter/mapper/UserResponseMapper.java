package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.input.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleResponseMapper.class})
public interface UserResponseMapper extends BaseResponseMapper<User, UserResponse> {
}
