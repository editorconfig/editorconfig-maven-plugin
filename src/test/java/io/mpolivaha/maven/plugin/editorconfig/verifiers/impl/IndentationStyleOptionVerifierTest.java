package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.common.CachingInputStream;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndentationStyleOptionVerifierTest {

  private final IndentationStyleOptionVerifier subject = new IndentationStyleOptionVerifier();

  @ParameterizedTest
  @MethodSource(value = {"arguments"})
  void testCharsetOptionVerifier(String sourceCodeFile, IndentationStyle indentationStyle, boolean noErrors) throws Exception {
    OptionValidationResult check = subject.check(
        new CachingInputStream(
            Paths.get(ClassLoader
                .getSystemClassLoader()
                .getResource(sourceCodeFile).toURI()).toFile()
        ),
        SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.indentationStyle(indentationStyle))
    );

    Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/charset/TestJavaClass_LATIN1.java", IndentationStyle.TAB, true)
    );
  }
}