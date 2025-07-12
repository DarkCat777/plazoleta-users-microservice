package com.pragma.users.domain.validation.rules;

import com.pragma.users.domain.validation.FieldValidationError;

import java.util.Optional;

public interface ValidationRule<T> {
    Optional<FieldValidationError> validate(T target);
}
