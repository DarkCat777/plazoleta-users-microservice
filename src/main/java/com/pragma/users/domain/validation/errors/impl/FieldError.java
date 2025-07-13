package com.pragma.users.domain.validation.errors.impl;

import com.pragma.users.domain.validation.errors.ValidationError;
import com.pragma.users.domain.validation.errors.WithField;
import com.pragma.users.domain.validation.errors.WithMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FieldError implements ValidationError, WithField, WithMessage {
    private final String field;
    private final Object rejectedValue;
    private final String message;
}
