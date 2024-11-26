package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

// TODO fix these test. It is always passing
@Disabled
class IndentationSizeOptionVerifierTest {

  private IndentationSizeOptionVerifier subject = new IndentationSizeOptionVerifier(Option.IDENT_STYLE);

  @ParameterizedTest
  @MethodSource(value = "source")
  void testIndentationSizeOptionVerifier(String sourceCodeFile, Integer tabWidth, Integer indentSize, boolean checkShouldPass) {
    OptionValidationResult result = subject.check(
        ClassLoader.getSystemClassLoader().getResourceAsStream(sourceCodeFile),
        SectionTestUtils.testSection(sb -> sb.tabWidth(tabWidth).indentationSize(indentSize))
    );

    Assertions.assertThat(result.noErrors()).isEqualTo(checkShouldPass);
  }

  static Stream<Arguments> source() {
    return Stream.of(
        Arguments.of("sources/indentation_size/TestJavaClass_IndentationTwo.java", 2, 2, true)
    );
  }
}