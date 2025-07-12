package com.pragma.users.domain.validation.rules.impl;

import com.pragma.users.domain.validation.CompositeValidationError;
import com.pragma.users.domain.validation.FieldValidationError;
import com.pragma.users.domain.validation.rules.ValidationRule;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class EachElementRule<T, E> implements ValidationRule<T> {

    private final String fieldName;
    private final Function<T, List<E>> extractor;
    private final Function<E, List<ValidationRule<E>>> validator;

    @Override
    public Optional<FieldValidationError> validate(T target) {
        List<E> value = extractor.apply(target);
        if (value == null) {
            return Optional.empty();
        }
        List<FieldValidationError> allErrors = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            E element = value.get(i);
            List<ValidationRule<E>> rules = validator.apply(element);
            for (ValidationRule<E> rule : rules) {
                Optional<FieldValidationError> optionalError = rule.validate(element);
                if (optionalError.isPresent()) {
                    String newField = "%s[%d].%s".formatted(fieldName, i, optionalError.get().getField());
                    allErrors.add(new FieldValidationError(newField, element, optionalError.get().getMessage()));
                }
            }
        }
        if (!allErrors.isEmpty()) {
            return Optional.of(new CompositeValidationError(fieldName, allErrors));
        }
        return Optional.empty();
    }
}
