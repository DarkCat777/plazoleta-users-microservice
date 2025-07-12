package com.pragma.users.domain.validation.extractor;

import java.util.function.Function;

@FunctionalInterface
public interface IntExtractor<T> extends Function<T, Integer> {
}