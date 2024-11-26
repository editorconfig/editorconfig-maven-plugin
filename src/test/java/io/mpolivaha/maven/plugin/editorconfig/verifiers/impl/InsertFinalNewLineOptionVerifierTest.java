package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
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
    subject = new InsertFinalNewLineOptionVerifier(Option.INSERT_FINAL_NEW_LINE);
  }

  @ParameterizedTest
  @MethodSource(value = "arguments")
  void testInsertFinalNewLineOptionVerifier(String sourceCodeFile, boolean shouldPass) {
    // given.
    OptionValidationResult sut = subject.check(
        ClassLoader.getSystemClassLoader().getResourceAsStream(sourceCodeFile),
        SectionTestUtils.testSection()
    );

    // when
    Assertions.assertThat(sut.noErrors()).isEqualTo(shouldPass);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/final_new_line/TestJavaClass_NoEOL.java", false),
        Arguments.of("sources/final_new_line/TestJavaClass_EOLPresent.java", true)
    );
  }

}