package com.pragma.users.domain.validation.exception;

import com.pragma.users.domain.validation.FieldValidationError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final List<FieldValidationError> errors;
}