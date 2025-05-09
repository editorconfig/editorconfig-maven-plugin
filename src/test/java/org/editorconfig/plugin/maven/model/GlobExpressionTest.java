package org.editorconfig.plugin.maven.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the {@link GlobExpression}.
 *
 * @author Mikhail Polivakha
 */
class GlobExpressionTest {

    @ParameterizedTest
    @MethodSource(value = "argumentsStream")
    void testFromRawString(String expected, String actual) {
        assertThat(GlobExpression.from(expected).getRaw()).isEqualTo(actual);
    }

    static Stream<Arguments> argumentsStream() {
        return Stream.of(
          Arguments.of("[*]", "*"),
          Arguments.of("[*/**]", "*/**"),
          Arguments.of("[*.{java,kt}]", "*.{java,kt}"),
          Arguments.of("[*.![c]]", "*.![c]")
        );
    }
}
