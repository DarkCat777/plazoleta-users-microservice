package com.pragma.users.infrastructure.adapter.mapper;

public interface BaseEntityMapper<D, E> {

    D toDomain(E entity);

    E toEntity(D domain);

}
