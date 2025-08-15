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
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.utils.IntegerUtils;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndentationSizeOptionVerifierTest {

    private IndentationSizeOptionVerifier subject;

    @BeforeEach
    void setUp() {
        subject = new IndentationSizeOptionVerifier();
    }

    @ParameterizedTest
    @MethodSource(value = "source")
    void check_IndentationSizeOptionVerifier_OptionalValidationResult(
            String sourceCodeFile, Integer tabWidth, Integer indentSize, boolean expected)
            throws URISyntaxException, FileNotFoundException {
        OptionValidationResult actual = subject.check(
                new CachingInputStream(new File(ClassLoader.getSystemClassLoader()
                        .getResource(sourceCodeFile)
                        .toURI())),
                SectionTestUtils.testSection(sb -> sb.tabWidth(OptionValue.resolve(
                                tabWidth.toString(), IntegerUtils::parseIntSafe))
                        .indentationSize(OptionValue.resolve(
                                indentSize.toString(), IntegerUtils::parseIntSafe))),
                new VerifiersExecutionContext());
        Assertions.assertThat(actual.noErrors()).isEqualTo(expected);
    }

    static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationTwo.java_test",
                        2,
                        2,
                        true),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationTwo.java_test",
                        2,
                        3,
                        false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test",
                        1,
                        5,
                        false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test",
                        4,
                        5,
                        true),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test",
                        4,
                        4,
                        false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test",
                        3,
                        5,
                        false));
    }
}
