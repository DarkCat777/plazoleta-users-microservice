package com.pragma.users.application.mapper;

public interface BaseResponseMapper<D, R> {
    R toResponse(D domain);
}
