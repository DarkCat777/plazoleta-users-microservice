package com.pragma.users.domain.validation.rules.impl;

import com.pragma.users.domain.validation.FieldValidationError;
import com.pragma.users.domain.validation.rules.ValidationRule;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class NotNullRule<T> implements ValidationRule<T> {

    private final String fieldName;
    private final Function<T, Object> extractor;
    private final String message;

    public static final String DEFAULT_MESSAGE = "No debe ser nulo.";

    public NotNullRule(String fieldName, Function<T, Object> extractor) {
        this(fieldName, extractor, DEFAULT_MESSAGE);
    }

    @Override
    public Optional<FieldValidationError> validate(T target) {
        Object value = extractor.apply(target);
        if (value == null)
            return Optional.of(new FieldValidationError(fieldName, value, MessageFormat.format(message, value)));
        return Optional.empty();
    }
}