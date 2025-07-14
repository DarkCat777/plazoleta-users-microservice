package com.pragma.users.infrastructure.output.feign.mapper;

import com.pragma.users.domain.model.Restaurant;
import com.pragma.users.infrastructure.output.feign.model.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantClientMapper {
    Restaurant toDomain(RestaurantResponse restaurant);
}
