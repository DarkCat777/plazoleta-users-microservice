package com.pragma.users.infrastructure.adapter.mapper;


public interface BaseMapper<D, E, R> {

    D toDomain(E entity);

    E toEntity(D domain);

    R toResponse(D domain);
}
