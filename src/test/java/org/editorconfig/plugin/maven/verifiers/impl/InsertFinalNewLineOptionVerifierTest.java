/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InsertFinalNewLineOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class InsertFinalNewLineOptionVerifierTest {

    private InsertFinalNewLineOptionVerifier subject;

    @BeforeEach
    void setUp() {
        subject = new InsertFinalNewLineOptionVerifier();
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
                        sectionBuilder -> sectionBuilder.insertFinalNewLine(TrueFalse.TRUE)),
                new VerifiersExecutionContext());

        // when
        Assertions.assertThat(sut.noErrors()).isEqualTo(shouldPass);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("sources/final_new_line/TestJavaClass_NoEOL.java", false),
                Arguments.of("sources/final_new_line/TestJavaClass_EOLPresent.java", true));
    }
}
