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
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EndOfLineOptionVerifierTest {

    private EndOfLineOptionVerifier subject;

    @BeforeEach
    void setUp() {
        subject = new EndOfLineOptionVerifier();
    }

    @ParameterizedTest
    @MethodSource(value = "arguments")
    void testEndOfLineOptionVerifier(String sourceCodeFile, EndOfLine endOfLine, boolean noErrors)
            throws URISyntaxException, FileNotFoundException {

        // when.
        OptionValidationResult check = subject.check(
                new CachingInputStream(new File(ClassLoader.getSystemClassLoader()
                        .getResource(sourceCodeFile)
                        .toURI())),
                SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.endOfLine(endOfLine)),
                new VerifiersExecutionContext());

        // then.
        Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_CR.java", EndOfLine.LINE_FEED, false),
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_CR.java",
                        EndOfLine.CARRIAGE_RERUN,
                        true),
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_LF.java",
                        EndOfLine.CARRIAGE_RERUN_LINE_FEED,
                        false),
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_LF.java", EndOfLine.LINE_FEED, true),
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_CRLF.java", EndOfLine.LINE_FEED, false),
                Arguments.of(
                        "sources/end_of_line/TestJavaClass_CRLF.java",
                        EndOfLine.CARRIAGE_RERUN_LINE_FEED,
                        true));
    }
}
