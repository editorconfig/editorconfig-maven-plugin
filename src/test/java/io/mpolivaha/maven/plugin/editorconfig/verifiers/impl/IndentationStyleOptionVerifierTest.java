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
      boolean noErrors
  ) throws Exception {
    OptionValidationResult check = subject.check(
        new CachingInputStream(
            Paths.get(ClassLoader
                .getSystemClassLoader()
                .getResource(sourceCodeFile).toURI()).toFile()
        ),
        SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.tabWidth(tabWidth).indentationStyle(indentationStyle))
    );

    Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/indentation_style/TestJavaClass_TabsOnly.java", IndentationStyle.TAB, 2, true),
        Arguments.of("sources/indentation_style/TestJavaClass_TabsOnly.java", IndentationStyle.SPACE, 2, false),
        Arguments.of("sources/indentation_style/TestJavaClass_SpacesOnly.java", IndentationStyle.TAB, 2, false),
        Arguments.of("sources/indentation_style/TestJavaClass_SpacesOnly.java", IndentationStyle.SPACE, 2, true),
        Arguments.of("sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java", IndentationStyle.SPACE, 2, false),
        Arguments.of("sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java", IndentationStyle.TAB, 2, false),
        Arguments.of("sources/indentation_style/TestJavaClass_SpacesMixedWithTabs.java", IndentationStyle.TAB, 8, true)
    );
  }
}