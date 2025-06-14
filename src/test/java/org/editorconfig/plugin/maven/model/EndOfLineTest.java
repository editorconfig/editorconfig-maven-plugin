/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class EndOfLineTest {

    @ParameterizedTest
    @EnumSource(value = EndOfLine.class, names = "CARRIAGE_RERUN_LINE_FEED", mode = Mode.EXCLUDE)
    void isSingleCharacter_СrLfEof_True(EndOfLine source) {

        // when/then
        Assertions.assertThat(source.isSingleCharacter()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = EndOfLine.class, names = "CARRIAGE_RERUN_LINE_FEED", mode = Mode.INCLUDE)
    void isSingleCharacter_Сrlf_False(EndOfLine source) {

        // when/then
        Assertions.assertThat(source.isSingleCharacter()).isFalse();
    }
}
