/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.apache.maven.plugin.MojoExecutionException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OptionValue}.
 *
 * @author Mikhail Polivakha
 */
class OptionValueTest {

    @Test
    void isUnset_OptionValueUnset_True() {
        // given
        OptionValue<Object> unset = OptionValue.unset();

        // when/then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(unset.isUnset()).isTrue();
            softAssertions
                    .assertThatThrownBy(unset::getValue)
                    .isInstanceOf(MojoExecutionException.class);
        });
    }

    @Test
    void resolve_NullSource_NullValue() {
        // given
        OptionValue<IndentationStyle> nullable = OptionValue.resolve(null, IndentationStyle::from);

        // when/then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(nullable.isUnset()).isFalse();

            softAssertions.assertThat(nullable.getValue()).isEqualTo(null);
        });
    }

    @Test
    void resolve_UnparsableSource_NullValue() {
        // given
        OptionValue<IndentationStyle> nullable = OptionValue.resolve("hey", IndentationStyle::from);

        // when/then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(nullable.isUnset()).isFalse();

            softAssertions.assertThat(nullable.getValue()).isEqualTo(null);
        });
    }

    @Test
    void resolve_TabSource_IndentationStyleTAB() {
        // given
        OptionValue<IndentationStyle> nullable = OptionValue.resolve("tab", IndentationStyle::from);

        // when/then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(nullable.isUnset()).isFalse();

            softAssertions.assertThat(nullable.getValue()).isEqualTo(IndentationStyle.TAB);
        });
    }
}
