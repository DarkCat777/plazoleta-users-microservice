package com.pragma.users.infrastructure.adapter.mapper;

public interface BaseResponseMapper<D, R> {
    R toResponse(D domain);
}
