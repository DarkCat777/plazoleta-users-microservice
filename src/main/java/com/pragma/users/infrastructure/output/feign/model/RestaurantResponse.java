package com.pragma.users.infrastructure.output.feign.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private String logoUrl;

    private String nit;

    private Long ownerId;

}