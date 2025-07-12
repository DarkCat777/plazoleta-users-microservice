package com.pragma.users.domain.validation;

import com.pragma.users.domain.validation.rules.ValidationRule;
import com.pragma.users.domain.validation.rules.impl.NotBlankRule;

import java.util.List;
import java.util.function.Function;

public class RuleChainBuilder<T> {

    @SafeVarargs
    public final List<ValidationRule<T>> chain(ValidationRule<T>... rules) {
        return List.of(rules);
    }

    public ValidationRule<T> notBlank(String field, Function<T, String> extractor) {
        return new NotBlankRule<>(field, extractor);
    }

    public ValidationRule<T> notBlank(String field, Function<T, String> extractor, String message) {
        return new NotBlankRule<>(field, extractor, message);
    }
}
