package com.pragma.users.domain.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String s) {
        super(s);
    }
}
