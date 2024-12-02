package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.context.ContextKeys;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TrimTrailingWhitespaceOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class TrimTrailingWhitespaceOptionVerifierTest {

  private TrimTrailingWhitespaceOptionVerifier subject;

  @BeforeEach
  void setUp() {
    subject = new TrimTrailingWhitespaceOptionVerifier(Option.TRIM_TRAILING_WHITESPACE);
  }

  @ParameterizedTest
  @MethodSource(value = "arguments")
  void testInsertFinalNewLineOptionVerifier(String sourceCodeFile, boolean shouldPass) {
    // given.
    OptionValidationResult sut = subject.check(
        ClassLoader.getSystemClassLoader().getResourceAsStream(sourceCodeFile),
        SectionTestUtils.testSection(),
        Map.of(ContextKeys.POSSIBLE_CHARSETS, List.of(Charset.UTF_8))
    );

    // when
    Assertions.assertThat(sut.noErrors()).isEqualTo(shouldPass);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/trim_trailing_whitespace/utf-8/TestJavaClass_UTF_8_No_Trailing_Whitespace.java", true),
        Arguments.of("sources/trim_trailing_whitespace/utf-8/TestJavaClass_UTF_8_Some_Trailing_Whitespace.java", false)
    );
  }
}