/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.nio.file.Paths;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link CharsetOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class CharsetOptionVerifierTest {

    private CharsetOptionVerifier subject;

    @BeforeEach
    void setUp() {
        subject = new CharsetOptionVerifier();
    }

    @ParameterizedTest
    @MethodSource(value = {"arguments"})
    void check_CharsetOptionVerifier_OptionalValidationResult(
            String sourceCodeFile, String charset, boolean expected) throws Exception {

        OptionValidationResult actual = subject.check(
                new CachingInputStream(Paths.get(ClassLoader.getSystemClassLoader()
                                .getResource(sourceCodeFile)
                                .toURI())
                        .toFile()),
                SectionTestUtils.testSection(sectionBuilder ->
                        sectionBuilder.charset(OptionValue.resolve(charset, Charset::from))),
                new VerifiersExecutionContext());

        Assertions.assertThat(actual.noErrors()).isEqualTo(expected);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("sources/charset/TestJavaClass_LATIN1.java", "latin1", true),
                Arguments.of("sources/charset/TestJavaClass_UTF8.java", "utf-8", true),
                Arguments.of("sources/charset/TestJavaClass_UTF8.java", "utf-16le", false),
                Arguments.of(
                        "sources/charset/TestJavaClass_UTF8_Cyrillic_Comments.java", "utf-8", true),
                Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", "utf-16be", true),
                Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", "utf-16le", false),
                Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", "utf-16le", true),
                Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", "latin1", false));
    }
}
