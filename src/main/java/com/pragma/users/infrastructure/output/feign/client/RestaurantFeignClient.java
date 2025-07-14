package com.pragma.users.infrastructure.output.feign.client;

import com.pragma.users.infrastructure.output.feign.model.RestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "restaurant-service", url = "${plazoleta.client.url}/restaurants")
public interface RestaurantFeignClient {

    @GetMapping("/owner")
    RestaurantResponse getRestaurantByOwner();
}