package com.pragma.users.domain.validation.extractor;

import java.util.function.Function;

@FunctionalInterface
public interface LongExtractor<T> extends Function<T, Long> {
}
