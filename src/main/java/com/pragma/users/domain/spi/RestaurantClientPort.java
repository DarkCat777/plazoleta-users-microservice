package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.Restaurant;

import java.util.Optional;

public interface RestaurantClientPort {
    Optional<Restaurant> findByOwner();
}
