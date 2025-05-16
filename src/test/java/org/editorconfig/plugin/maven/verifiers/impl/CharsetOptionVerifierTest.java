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
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link CharsetOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class CharsetOptionVerifierTest {

    private final CharsetOptionVerifier subject = new CharsetOptionVerifier();

    @ParameterizedTest
    @MethodSource(value = {"arguments"})
    void testCharsetOptionVerifier(String sourceCodeFile, Charset charset, boolean noErrors)
            throws Exception {

        OptionValidationResult check = subject.check(
                new CachingInputStream(Paths.get(ClassLoader.getSystemClassLoader()
                                .getResource(sourceCodeFile)
                                .toURI())
                        .toFile()),
                SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.charset(charset)),
                new VerifiersExecutionContext());

        Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("sources/charset/TestJavaClass_LATIN1.java", Charset.LATIN1, true),
                Arguments.of("sources/charset/TestJavaClass_UTF8.java", Charset.UTF_8, true),
                Arguments.of("sources/charset/TestJavaClass_UTF8.java", Charset.UTF_16LE, false),
                Arguments.of(
                        "sources/charset/TestJavaClass_UTF8_Cyrillic_Comments.java",
                        Charset.UTF_8,
                        true),
                Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", Charset.UTF_16BE, true),
                Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", Charset.UTF_16LE, false),
                Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", Charset.UTF_16LE, true),
                Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", Charset.LATIN1, false));
    }
}
