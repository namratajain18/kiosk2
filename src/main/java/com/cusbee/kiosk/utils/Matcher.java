package com.cusbee.kiosk.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Matcher<R, T> {

    static <R, T> Matcher<R, T> when(Predicate<R> predicate, Function<R, T> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);

        return value -> predicate.test(value) ? Optional.of(action.apply(value)) : Optional.empty();
    }

    default Matcher<R, T> either(Function<R, T> action) {
        Objects.requireNonNull(action);

        return value -> {
            final Optional<T> result = match(value);
            return result.isPresent() ? result : Optional.of(action.apply(value));
        };
    }

    default Matcher<R, T> or(Predicate<R> predicate, Function<R, T> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);

        return value -> {
            final Optional<T> result = match(value);
            if (result.isPresent()) {
                return result;
            } else {
                return predicate.test(value) ? Optional.of(action.apply(value)) : Optional.empty();
            }
        };
    }

    default T flatMatch(R value) {
        return match(value).get();
    }

    Optional<T> match(R value);
}
