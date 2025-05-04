/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndentationSizeOptionVerifierTest {

    private final IndentationSizeOptionVerifier subject = new IndentationSizeOptionVerifier();

    @ParameterizedTest
    @MethodSource(value = "source")
    void testIndentationSizeOptionVerifier(
            String sourceCodeFile, Integer tabWidth, Integer indentSize, boolean checkShouldPass)
            throws URISyntaxException, FileNotFoundException {
        OptionValidationResult result = subject.check(
                new CachingInputStream(new File(ClassLoader.getSystemClassLoader()
                        .getResource(sourceCodeFile)
                        .toURI())),
                SectionTestUtils.testSection(
                        sb -> sb.tabWidth(tabWidth).indentationSize(indentSize)),
                new HashMap<>());
        Assertions.assertThat(result.noErrors()).isEqualTo(checkShouldPass);
    }

    static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationTwo.java_test", 2, 2, true),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationTwo.java_test", 2, 3, false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test", 1, 5, false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test", 4, 5, true),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test", 4, 4, false),
                Arguments.of(
                        "sources/indentation_size/TestJavaClass_IndentationFive.java_test", 3, 5, false));
    }
}
