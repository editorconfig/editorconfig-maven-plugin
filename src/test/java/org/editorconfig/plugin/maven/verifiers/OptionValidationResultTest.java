/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import org.assertj.core.api.SoftAssertions;
import org.editorconfig.plugin.maven.model.Option;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OptionValidationResult}
 *
 * @author Mikhail Polivakha
 */
class OptionValidationResultTest {

    @Test
    void addErrorMessage_Violations_ExactViolationsCount() {
        // given
        var first = new OptionValidationResult(Option.CHARSET, "utf-8");
        var second = new OptionValidationResult(Option.CHARSET, "utf-8");

        // when
        second.addErrorMessage("Some error message!");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(first.violationsCount()).isEqualTo(0);
            softAssertions.assertThat(second.violationsCount()).isEqualTo(1);
            softAssertions.assertThat(first.noErrors()).isEqualTo(true);
            softAssertions.assertThat(second.noErrors()).isEqualTo(false);
        });
    }

    @Test
    void renderErrorMessage_Violations_ValidErrorMessage() {
        // given
        var first = new OptionValidationResult(Option.CHARSET, "utf-8");
        first.addErrorMessage("First Error!");
        first.addErrorMessage("Second Error!");

        // when
        String errorMessage = first.renderErrorMessage();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(first.noErrors()).isEqualTo(false);
            softAssertions.assertThat(first.violationsCount()).isEqualTo(2);
            softAssertions
                    .assertThat(errorMessage)
                    .isEqualTo("For option charset=utf-8 found 2 violation(-s):\n"
                            + "\t- First Error!\n"
                            + "\t- Second Error!\n");
        });
    }
}
