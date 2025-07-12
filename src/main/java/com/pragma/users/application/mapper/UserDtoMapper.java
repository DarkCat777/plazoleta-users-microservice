package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.request.CreateUserCommand;
import com.pragma.users.application.dto.response.UserResponse;
import com.pragma.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleDtoMapper.class})
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true) // no viene del request
    @Mapping(target = "role", ignore = true) // se asigna en la capa de dominio
    User toDomain(CreateUserCommand request);

    UserResponse toResponse(User domain);
}
