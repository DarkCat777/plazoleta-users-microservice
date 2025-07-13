package com.pragma.users.domain.validation.rules;

import com.pragma.users.domain.validation.errors.ValidationError;

import java.util.Optional;

public interface ValidationRule<T> {
    Optional<ValidationError> validate(T target);
}
