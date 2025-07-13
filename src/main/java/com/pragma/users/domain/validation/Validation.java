package com.pragma.users.domain.validation;

import com.pragma.users.domain.validation.errors.ValidationError;
import com.pragma.users.domain.validation.exception.ValidationException;
import com.pragma.users.domain.validation.rules.ValidationRule;
import com.pragma.users.domain.validation.rules.extractor.*;
import com.pragma.users.domain.validation.rules.impl.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Validation<T> {

    private final T object;
    private final List<ValidationRule<T>> rules;

    public static <T> Builder<T> builder(T object) {
        return new Builder<>(object);
    }

    public void validate() throws ValidationException {
        List<ValidationError> errors = rules.stream()
                .map(rule -> rule.validate(object))
                .flatMap(Optional::stream)
                .toList();
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @AllArgsConstructor
    public static class Builder<T> {

        private final T object;
        private final List<ValidationRule<T>> rules = new ArrayList<>();

        public Builder<T> email(String fieldName, StringExtractor<T> extractor, String message) {
            rules.add(new EmailRule<>(fieldName, extractor, message));
            return this;
        }

        public Builder<T> email(String fieldName, StringExtractor<T> extractor) {
            rules.add(new EmailRule<>(fieldName, extractor));
            return this;
        }

        public Builder<T> notBlank(String fieldName, StringExtractor<T> extractor) {
            rules.add(new NotBlankRule<>(fieldName, extractor));
            return this;
        }

        public Builder<T> notNull(String fieldName, Function<T, ?> extractor) {
            rules.add(new NotNullRule<>(fieldName, extractor));
            return this;
        }

        public Builder<T> pattern(String fieldName, StringExtractor<T> extractor, String pattern) {
            rules.add(new PatternRule<>(fieldName, extractor, pattern));
            return this;
        }

        public Builder<T> minYearDifference(String field, Function<T, LocalDate> extractor, int minYears) {
            rules.add(new MinYearDifferenceRule<>(field, t -> LocalDate.now(), extractor, minYears));
            return this;
        }

        public Validation<T> build() {
            return new Validation<>(object, rules);
        }

    }

}
