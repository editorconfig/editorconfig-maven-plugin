/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.apache.maven.plugin.MojoExecutionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OptionValue}.
 *
 * @author Mikhail Polivakha
 */
class OptionValueTest {

    @Test
    void testUnset() {
        // given.
        OptionValue<Object> unset = OptionValue.unset();

        // when/then.
        Assertions.assertThat(unset.isUnset()).isTrue();
        Assertions.assertThatThrownBy(unset::getValue).isInstanceOf(MojoExecutionException.class);
    }

    @Test
    void testResolve_null() {
        // given.
        OptionValue<IndentationStyle> nullable = OptionValue.resolve(null, IndentationStyle::from);

        // when/then.
        Assertions.assertThat(nullable.isUnset()).isFalse();
        Assertions.assertThat(nullable.getValue()).isEqualTo(null);
    }

    @Test
    void testResolve_unparsable() {
        // given.
        OptionValue<IndentationStyle> nullable = OptionValue.resolve("hey", IndentationStyle::from);

        // when/then.
        Assertions.assertThat(nullable.isUnset()).isFalse();
        Assertions.assertThat(nullable.getValue()).isEqualTo(null);
    }

    @Test
    void testResolve_validValue() {
        // given.
        OptionValue<IndentationStyle> nullable = OptionValue.resolve("tab", IndentationStyle::from);

        // when/then.
        Assertions.assertThat(nullable.isUnset()).isFalse();
        Assertions.assertThat(nullable.getValue()).isEqualTo(IndentationStyle.TAB);
    }
}
