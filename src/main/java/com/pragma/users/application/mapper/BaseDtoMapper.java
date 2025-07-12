package com.pragma.users.application.mapper;

public interface BaseDtoMapper<D, R, T> extends
        BaseRequestMapper<D, R>,
        BaseResponseMapper<D, T> {
}
