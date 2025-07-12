package com.pragma.users.application.mapper;

public interface BaseRequestMapper<D, R> {
    D toDomain(R request);
}
