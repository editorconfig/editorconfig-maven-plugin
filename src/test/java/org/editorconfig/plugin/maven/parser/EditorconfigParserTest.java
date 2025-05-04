/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.parser;

import java.nio.file.Paths;
import java.util.Map;

import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.config.PluginConfiguration.Param;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.IndentationStyle;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link EditorconfigParser}
 *
 * @author Mikhail Polivakha
 */
class EditorconfigParserTest {

    @AfterEach
    void tearDown() {
        PluginConfiguration.destroyInstance();
    }

    @Test
    void testFileParsing_inStrictMode_happyPath() {
        // Given.
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, true));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When.
        String path = testClassPathResource("test-files/.root-editorconfig");
        Editorconfig result = editorconfigParser.parse(Paths.get(path));

        // Then.
        assertThat(result.isRoot()).isTrue();
        assertThat(result.getSections()).hasSize(2);

        assertThat(result.getSections().get(0)).satisfies(section -> {
            assertThat(section.getGlobExpression().getRaw())
                    .isEqualTo("[{file.py,another-file.py}]");
            assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.TAB);
            assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
            assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
            assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.TRUE);
            assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.FALSE);
            assertThat(section.getIndentationSize()).isEqualTo(2);
        });

        assertThat(result.getSections().get(1)).satisfies(section -> {
            assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
            assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
            assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
            assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
            assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.FALSE);
            assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.TRUE);
            assertThat(section.getIndentationSize()).isNull();
        });
    }

    @Test
    void testFileParsing_inNonStrictMode_happyPath() {
        // Given.
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When.
        String path = testClassPathResource("test-files/.root-editorconfig");
        Editorconfig result = editorconfigParser.parse(Paths.get(path));

        // Then.
        assertThat(result.isRoot()).isTrue();
        assertThat(result.getSections()).hasSize(2);

        assertThat(result.getSections().get(0)).satisfies(section -> {
            assertThat(section.getGlobExpression().getRaw())
                    .isEqualTo("[{file.py,another-file.py}]");
            assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.TAB);
            assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
            assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
            assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.TRUE);
            assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.FALSE);
        });

        assertThat(result.getSections().get(1)).satisfies(section -> {
            assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
            assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
            assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
            assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
            assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.FALSE);
            assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.TRUE);
        });
    }

    @Test
    void testFileParsing_inNonStrictMode_ErrorsDuringParsing() {
        // Given.
        PluginConfiguration.buildInstance(Map.of(
                Param.STRICT_MODE,
                false,
                Param.LOG,
                new DefaultLog(new ConsoleLogger(Logger.LEVEL_INFO, "TestLogger"))));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        // When.
        String path = testClassPathResource("test-files/.invalid-editorconfig");
        Editorconfig result = editorconfigParser.parse(Paths.get(path));

        // Then.
        assertThat(result.isRoot()).isTrue();
        assertThat(result.getSections()).hasSize(2);
        assertThat(result.getSections().get(1)).satisfies(section -> {
            assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
            assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
            assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
            assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
        });
    }

    @Test
    void testFileParsing_shouldTestJustRootEditorConfig() {
        PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false));
        EditorconfigParser editorconfigParser = new EditorconfigParser();

        String path = testClassPathResource("test-files/.just-root");

        Editorconfig parsed = editorconfigParser.parse(Paths.get(path));

        assertThat(parsed.isRoot()).isTrue();
        assertThat(parsed.getSections()).isEmpty();
    }

    private static String testClassPathResource(String name) {
        return ClassLoader.getSystemClassLoader().getResource(name).getPath();
    }
}
