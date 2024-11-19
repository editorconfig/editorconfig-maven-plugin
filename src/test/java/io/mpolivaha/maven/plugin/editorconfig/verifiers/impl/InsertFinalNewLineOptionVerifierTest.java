package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InsertFinalNewLineOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class InsertFinalNewLineOptionVerifierTest {

  private InsertFinalNewLineOptionVerifier subject;

  @BeforeEach
  void setUp() {
    subject = new InsertFinalNewLineOptionVerifier(Option.INSERT_FINAL_NEW_LINE);
  }

  @Test
  void test_noEOL() throws IOException {
    // given.
    OptionValidationResult sut = subject.check(
        ClassLoader.getSystemClassLoader().getResourceAsStream("sources/final_new_line/TestJavaClass_NoEOL.java"),
        Section
            .builder()
            .endOfLine(EndOfLine.LINE_FEED)
            .charset(Charset.UTF_8)
            .indentationSize(2)
            .indentationStyle(IndentationStyle.TAB)
            .build().getSections().get(0)
    );

    // when
    Assertions.assertThat(sut.noErrors()).isFalse();
    Assertions.assertThat(sut.renderErrorMessage()).isEqualTo(
        "For option insert_final_newline=null found 1 violation(-s):\n"
        + "\t- Expected the end_of_line symbol to be present, but file terminates with EOF\n");
  }

  @Test
  void test_EOLPresent() {
    // given.
    OptionValidationResult sut = subject.check(
        ClassLoader.getSystemClassLoader().getResourceAsStream("sources/final_new_line/TestJavaClass_EOLPresent.java"),
        Section
            .builder()
            .endOfLine(EndOfLine.LINE_FEED)
            .charset(Charset.UTF_8)
            .indentationSize(2)
            .indentationStyle(IndentationStyle.TAB)
            .build().getSections().get(0)
    );

    // when
    Assertions.assertThat(sut.noErrors()).isTrue();
  }
}