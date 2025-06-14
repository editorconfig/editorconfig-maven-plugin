/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void OptionFrom_EndOfLine_ValidNameAndKey() {

        // when/then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions
                    .assertThat(Option.from(Option.END_OF_LINE.name()))
                    .isPresent()
                    .hasValue(Option.END_OF_LINE);

            softAssertions
                    .assertThat(Option.from(Option.END_OF_LINE.getKey()))
                    .isPresent()
                    .hasValue(Option.END_OF_LINE);
        });
    }
}
