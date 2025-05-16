/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.editorconfig.plugin.maven.verifiers.context.ContextKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TrimTrailingWhitespaceOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class TrimTrailingWhitespaceOptionVerifierTest {

    private TrimTrailingWhitespaceOptionVerifier subject;

    @BeforeEach
    void setUp() {
        subject = new TrimTrailingWhitespaceOptionVerifier();
    }

    @ParameterizedTest
    @MethodSource(value = "arguments")
    void testInsertFinalNewLineOptionVerifier(String sourceCodeFile, boolean shouldPass)
            throws URISyntaxException, FileNotFoundException {
        // given.
        OptionValidationResult sut = subject.check(
                new CachingInputStream(new File(ClassLoader.getSystemClassLoader()
                        .getResource(sourceCodeFile)
                        .toURI())),
                SectionTestUtils.testSection(
                        sectionBuilder -> sectionBuilder.trimTrailingWhitespace(TrueFalse.TRUE)),
                new VerifiersExecutionContext()
                        .putGlobal(ContextKeys.POSSIBLE_CHARSETS, List.of(Charset.UTF_8)));

        // when
        Assertions.assertThat(sut.noErrors()).isEqualTo(shouldPass);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(
                        "sources/trim_trailing_whitespace/utf-8/TestJavaClass_UTF_8_No_Trailing_Whitespace.java",
                        true),
                Arguments.of(
                        "sources/trim_trailing_whitespace/utf-8/TestJavaClass_UTF_8_Some_Trailing_Whitespace.java",
                        false),
                Arguments.of(
                        "sources/trim_trailing_whitespace/utf-16be/TestJavaClass_UTF16BE_No_Trailing_Whitespace.java",
                        true),
                Arguments.of(
                        "sources/trim_trailing_whitespace/utf-16le/TestJavaClass_UTF16LE_No_Trailing_Whitespace.java",
                        true),
                Arguments.of(
                        "sources/trim_trailing_whitespace/utf-16le/TestJavaClass_UTF16LE_Some_Trailing_Whitespace.java",
                        false));
    }
}
