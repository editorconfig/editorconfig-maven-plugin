/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.nio.file.Paths;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.model.IndentationStyle;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.utils.IntegerUtils;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link IndentationStyleOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class IndentationStyleOptionVerifierTest {

    private final IndentationStyleOptionVerifier subject = new IndentationStyleOptionVerifier();

    @ParameterizedTest
    @MethodSource(value = {"arguments"})
    void testIndentationStyleOptionVerifier(
            String sourceCodeFile,
            IndentationStyle indentationStyle,
            Integer tabWidth,
            boolean noErrors)
            throws Exception {
        OptionValidationResult check = subject.check(
                new CachingInputStream(Paths.get(ClassLoader.getSystemClassLoader()
                                .getResource(sourceCodeFile)
                                .toURI())
                        .toFile()),
                SectionTestUtils.testSection(sectionBuilder -> sectionBuilder
                        .tabWidth(OptionValue.resolve(
                                tabWidth.toString(), IntegerUtils::parseIntSafe))
                        .indentationStyle(OptionValue.resolve(
                                indentationStyle.name(), IndentationStyle::from))),
                new VerifiersExecutionContext());

        Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_TabsOnly.java",
                        IndentationStyle.TAB,
                        2,
                        true),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_TabsOnly.java",
                        IndentationStyle.SPACE,
                        2,
                        false),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_SpacesOnly.java",
                        IndentationStyle.TAB,
                        2,
                        false),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_SpacesOnly.java",
                        IndentationStyle.SPACE,
                        2,
                        true),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java",
                        IndentationStyle.SPACE,
                        2,
                        false),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java",
                        IndentationStyle.TAB,
                        2,
                        false),
                Arguments.of(
                        "sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java",
                        IndentationStyle.TAB,
                        8,
                        true));
    }
}
