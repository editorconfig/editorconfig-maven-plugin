/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.parser;

import java.nio.file.Paths;
import java.util.Map;

import org.apache.maven.monitor.logging.DefaultLog;
import org.assertj.core.api.SoftAssertions;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.config.PluginConfiguration.Param;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.IndentationStyle;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link EditorconfigParser}
 *
 * @author Mikhail Polivakha
 */
class EditorconfigParserTest {

    @BeforeEach
    void tearDown() {
        PluginConfiguration.destroyInstance();
    }

    @Test
    void parse_inStrictMode_NoErrorsDuringParsing() {
        // Given
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, true));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When
        String path = testClassPathResource("test-files/.root-editorconfig");
        Editorconfig actual = editorconfigParser.parse(Paths.get(path));

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.isRoot()).isTrue();
            softAssertions.assertThat(actual.getSections()).hasSize(2);
            softAssertions.assertThat(actual.getSections().get(0)).satisfies(section -> {
                assertThat(section.getGlobExpression().getRaw())
                        .isEqualTo("{file.py,another-file.py}");
                assertThat(section.getIndentationStyle().getValue())
                        .isEqualTo(IndentationStyle.TAB);
                assertThat(section.getEndOfLine().getValue()).isEqualTo(EndOfLine.LINE_FEED);
                assertThat(section.getCharset().getValue()).isEqualTo(Charset.UTF_8);
                assertThat(section.getTrimTrailingWhitespace().getValue())
                        .isEqualTo(TrueFalse.TRUE);
                assertThat(section.getInsertFinalNewLine().getValue()).isEqualTo(TrueFalse.FALSE);
                assertThat(section.getIndentationSizeAsDigit()).isEqualTo(2);
            });
            softAssertions.assertThat(actual.getSections().get(1)).satisfies(section -> {
                assertThat(section.getGlobExpression().getRaw()).isEqualTo("*.{.kt,.java}");
                assertThat(section.getIndentationStyle().getValue())
                        .isEqualTo(IndentationStyle.SPACE);
                assertThat(section.getEndOfLine().getValue())
                        .isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
                assertThat(section.getCharset().getValue()).isEqualTo(Charset.UTF_8);
                assertThat(section.getTrimTrailingWhitespace().getValue())
                        .isEqualTo(TrueFalse.FALSE);
                assertThat(section.getInsertFinalNewLine().getValue()).isEqualTo(TrueFalse.TRUE);
                assertThat(section.getIndentationSizeAsDigit()).isNull();
            });
        });
    }

    @Test
    void parse_inNonStrictMode_NoErrorsDuringParsing() {
        // Given
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When
        String path = testClassPathResource("test-files/.root-editorconfig");
        Editorconfig result = editorconfigParser.parse(Paths.get(path));

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.isRoot()).isTrue();
            softAssertions.assertThat(result.getSections()).hasSize(2);
            softAssertions.assertThat(result.getSections().get(0)).satisfies(section -> {
                assertThat(section.getGlobExpression().getRaw())
                        .isEqualTo("{file.py,another-file.py}");
                assertThat(section.getIndentationStyle().getValue())
                        .isEqualTo(IndentationStyle.TAB);
                assertThat(section.getEndOfLine().getValue()).isEqualTo(EndOfLine.LINE_FEED);
                assertThat(section.getCharset().getValue()).isEqualTo(Charset.UTF_8);
                assertThat(section.getTrimTrailingWhitespace().getValue())
                        .isEqualTo(TrueFalse.TRUE);
                assertThat(section.getInsertFinalNewLine().getValue()).isEqualTo(TrueFalse.FALSE);
            });
            softAssertions.assertThat(result.getSections().get(1)).satisfies(section -> {
                assertThat(section.getGlobExpression().getRaw()).isEqualTo("*.{.kt,.java}");
                assertThat(section.getIndentationStyle().getValue())
                        .isEqualTo(IndentationStyle.SPACE);
                assertThat(section.getEndOfLine().getValue())
                        .isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
                assertThat(section.getCharset().getValue()).isEqualTo(Charset.UTF_8);
                assertThat(section.getTrimTrailingWhitespace().getValue())
                        .isEqualTo(TrueFalse.FALSE);
                assertThat(section.getInsertFinalNewLine().getValue()).isEqualTo(TrueFalse.TRUE);
            });
        });
    }

    @Test
    void parse_inNonStrictMode_ErrorsDuringParsing() {
        // Given
        PluginConfiguration.buildInstance(Map.of(
                Param.STRICT_MODE,
                false,
                Param.LOG,
                new DefaultLog(new ConsoleLogger(Logger.LEVEL_INFO, "TestLogger"))));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When
        String path = testClassPathResource("test-files/.invalid-editorconfig");
        Editorconfig result = editorconfigParser.parse(Paths.get(path));

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.isRoot()).isTrue();
            softAssertions.assertThat(result.getSections()).hasSize(2);
            softAssertions.assertThat(result.getSections().get(1)).satisfies(section -> {
                assertThat(section.getGlobExpression().getRaw()).isEqualTo("*.{.kt,.java}");
                assertThat(section.getIndentationStyle().getValue())
                        .isEqualTo(IndentationStyle.SPACE);
                assertThat(section.getEndOfLine().getValue())
                        .isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
                assertThat(section.getCharset().getValue()).isEqualTo(Charset.UTF_8);
            });
        });
    }

    @Test
    void parse_inNonStrictModeAndEmptySections_NoErrorsDuringParsing() {
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        String path = testClassPathResource("test-files/.just-root");

        Editorconfig parsed = editorconfigParser.parse(Paths.get(path));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(parsed.isRoot()).isTrue();
            softAssertions.assertThat(parsed.getSections()).isEmpty();
        });
    }

    private static String testClassPathResource(String name) {
        return ClassLoader.getSystemClassLoader().getResource(name).getPath();
    }
}
