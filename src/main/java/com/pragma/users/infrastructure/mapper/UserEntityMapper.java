package com.pragma.users.infrastructure.mapper;

import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.adapter.output.model.JpaUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleEntityMapper.class})
public interface UserEntityMapper extends BaseEntityMapper<User, JpaUserEntity> {
}
