package com.pragma.users.infrastructure.output.jpa.mapper;

import com.pragma.users.domain.model.User;
import com.pragma.users.infrastructure.output.jpa.model.JpaUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleEntityMapper.class})
public interface UserEntityMapper {

    User toDomain(JpaUserEntity entity);

    JpaUserEntity toEntity(User domain);

}
