package com.pragma.users.infrastructure.adapter.mapper;

import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.input.dto.UserResponse;
import com.pragma.users.infrastructure.adapter.output.model.JpaUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleMapper.class})
public interface UserMapper extends BaseMapper<User, JpaUserEntity, UserResponse> {
}
