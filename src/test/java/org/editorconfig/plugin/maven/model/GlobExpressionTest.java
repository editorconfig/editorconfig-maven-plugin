/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link GlobExpression}.
 *
 * @author Mikhail Polivakha
 */
class GlobExpressionTest {

    @ParameterizedTest
    @MethodSource(value = "argumentsStream")
    void fromAndGetRaw_ListOfRawStrings_StringValues(String expected, String actual) {

        // when/then
        assertThat(GlobExpression.from(expected).getRaw()).isEqualTo(actual);
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of("[*]", "*"),
                Arguments.of("[*/**]", "*/**"),
                Arguments.of("[*.{java,kt}]", "*.{java,kt}"),
                Arguments.of("[*.![c]]", "*.![c]"));
    }
}
