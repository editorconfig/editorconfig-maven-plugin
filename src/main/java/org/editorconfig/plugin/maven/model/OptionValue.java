/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.util.Optional;
import java.util.function.Function;

import org.editorconfig.plugin.maven.assertions.Assert;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * The discriminated union type between the {@code unset} and the actual value.
 *
 * @apiNote See <a href="https://spec.editorconfig.org/#supported-pairs">supported pairs for clarification</a>
 * @param <T> the type of the actual value
 * @author Mikhail Polivakha
 */
public class OptionValue<T> {

    private final @Nullable T value;

    /**
     * The value equals to unset
     */
    private final boolean unset;

    /**
     * The value was recognized by the parser or not
     */
    private final boolean recognized;

    public static final String UNSET = "unset";

    public OptionValue(@Nullable T value, boolean unset, boolean recognized) {
        this.value = value;
        this.unset = unset;
        this.recognized = recognized;
    }

    public static <T> OptionValue<T> unset() {
        return new OptionValue<>(null, true, true);
    }

    public static <T> OptionValue<T> fromValue(T value) {
        return new OptionValue<>(value, false, true);
    }

    private static <T> OptionValue<T> unrecognized() {
        return new OptionValue<>(null, false, false);
    }

    public static <T> OptionValue<T> resolve( //
            @Nullable String source, //
            Function<@NonNull String, Optional<T>> valueProvider //
            ) {

        // null is NOT equal to unset
        if (source == null) {
            return OptionValue.fromValue(null);
        }

        if (source.equalsIgnoreCase(UNSET)) {
            return OptionValue.unset();
        }

        return valueProvider
                .apply(source)
                .map(OptionValue::fromValue)
                .orElseGet(OptionValue::unrecognized);
    }

    public boolean isUnset() {
        return unset;
    }

    public boolean isRecognized() {
        return recognized;
    }

    /**
     * @return null in case the option was not set, or the value of an option was not recognized
     */
    public @Nullable T getValue() {
        Assert.state(() -> !unset, "The value is unset");
        return value;
    }
}
