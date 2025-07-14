package com.pragma.users.infrastructure.output.feign.adapter;

import com.pragma.users.domain.model.Restaurant;
import com.pragma.users.domain.spi.RestaurantClientPort;
import com.pragma.users.infrastructure.exception.ExternalServiceException;
import com.pragma.users.infrastructure.output.feign.client.RestaurantFeignClient;
import com.pragma.users.infrastructure.output.feign.mapper.RestaurantClientMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantClientAdapter implements RestaurantClientPort {

    private final RestaurantFeignClient restaurantFeignClient;
    private final RestaurantClientMapper restaurantClientMapper;

    @Override
    public Optional<Restaurant> findByOwner() {
        try {
            return Optional.of(restaurantClientMapper.toDomain(restaurantFeignClient.getRestaurantByOwner()));
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        } catch (FeignException ex) {
            throw new ExternalServiceException("Error al comunicar con servicio de usuarios: " + ex.getMessage(), ex);
        }
    }
}
