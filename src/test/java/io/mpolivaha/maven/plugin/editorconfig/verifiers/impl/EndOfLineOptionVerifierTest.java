package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
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
  void testEndOfLineOptionVerifier(String sourceCodeFile, EndOfLine endOfLine, boolean noErrors) {

    // when.
    OptionValidationResult check = subject.check(
        ClassLoader
            .getSystemClassLoader()
            .getResourceAsStream(sourceCodeFile),
        SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.endOfLine(endOfLine))
    );

    // then.
    Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/end_of_line/TestJavaClass_CR.java", EndOfLine.LINE_FEED, false),
        Arguments.of("sources/end_of_line/TestJavaClass_CR.java", EndOfLine.CARRIAGE_RERUN, true),
        Arguments.of("sources/end_of_line/TestJavaClass_LF.java", EndOfLine.CARRIAGE_RERUN_LINE_FEED, false),
        Arguments.of("sources/end_of_line/TestJavaClass_LF.java", EndOfLine.LINE_FEED, true),
        Arguments.of("sources/end_of_line/TestJavaClass_CRLF.java", EndOfLine.LINE_FEED, false),
        Arguments.of("sources/end_of_line/TestJavaClass_CRLF.java", EndOfLine.CARRIAGE_RERUN_LINE_FEED, true)
    );
  }
}