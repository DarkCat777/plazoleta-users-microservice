package com.pragma.users.infrastructure.mapper;

public interface BaseEntityMapper<D, E> {

    D toDomain(E entity);

    E toEntity(D domain);

}
