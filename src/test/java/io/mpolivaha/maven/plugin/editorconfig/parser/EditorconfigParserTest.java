package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.maven.monitor.logging.DefaultLog;
import org.assertj.core.api.Assertions;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

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
    String path = ClassLoader.getSystemClassLoader().getResource("test-files/.root-editorconfig").getPath();
    Editorconfig result = editorconfigParser.parse(Paths.get(path));

    // Then.
    Assertions.assertThat(result.isRoot()).isTrue();
    Assertions.assertThat(result.getSections()).hasSize(2);

    Assertions.assertThat(result.getSections().get(0)).satisfies(section -> {
      Assertions.assertThat(section.getGlobExpression().getRaw()).isEqualTo("[{file.py,another-file.py}]");
      Assertions.assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.TAB);
      Assertions.assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
      Assertions.assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
      Assertions.assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.TRUE);
      Assertions.assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.FALSE);
      Assertions.assertThat(section.getIndentationSize()).isEqualTo(2);
    });

    Assertions.assertThat(result.getSections().get(1)).satisfies(section -> {
      Assertions.assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
      Assertions.assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
      Assertions.assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
      Assertions.assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
      Assertions.assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.FALSE);
      Assertions.assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.TRUE);
      Assertions.assertThat(section.getIndentationSize()).isNull();
    });
  }

  @Test
  void testFileParsing_inNonStrictMode_happyPath() {
    // Given.
    PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false));
    EditorconfigParser editorconfigParser = new EditorconfigParser();

    // When.
    String path = ClassLoader.getSystemClassLoader().getResource("test-files/.root-editorconfig").getPath();
    Editorconfig result = editorconfigParser.parse(Paths.get(path));

    // Then.
    Assertions.assertThat(result.isRoot()).isTrue();
    Assertions.assertThat(result.getSections()).hasSize(2);

    Assertions.assertThat(result.getSections().get(0)).satisfies(section -> {
      Assertions.assertThat(section.getGlobExpression().getRaw()).isEqualTo("[{file.py,another-file.py}]");
      Assertions.assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.TAB);
      Assertions.assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
      Assertions.assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
      Assertions.assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.TRUE);
      Assertions.assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.FALSE);
    });

    Assertions.assertThat(result.getSections().get(1)).satisfies(section -> {
      Assertions.assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
      Assertions.assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
      Assertions.assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
      Assertions.assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
      Assertions.assertThat(section.getTrimTrailingWhitespace()).isEqualTo(TrueFalse.FALSE);
      Assertions.assertThat(section.getInsertFinalNewLine()).isEqualTo(TrueFalse.TRUE);
    });
  }

  @Test
  void testFileParsing_inNonStrictMode_ErrorsDuringParsing() {
    // Given.
    PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, false, Param.LOG, new DefaultLog(new ConsoleLogger())));
    EditorconfigParser editorconfigParser = new EditorconfigParser();

    // When.
    String path = ClassLoader.getSystemClassLoader().getResource("test-files/.invalid-editorconfig").getPath();
    Editorconfig result = editorconfigParser.parse(Paths.get(path));

    // Then.
    Assertions.assertThat(result.isRoot()).isTrue();
    Assertions.assertThat(result.getSections()).hasSize(2);
    Assertions.assertThat(result.getSections().get(1)).satisfies(section -> {
      Assertions.assertThat(section.getGlobExpression().getRaw()).isEqualTo("[*.{.kt,.java}]");
      Assertions.assertThat(section.getIndentationStyle()).isEqualTo(IndentationStyle.SPACE);
      Assertions.assertThat(section.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
      Assertions.assertThat(section.getCharset()).isEqualTo(Charset.UTF_8);
    });
  }
}