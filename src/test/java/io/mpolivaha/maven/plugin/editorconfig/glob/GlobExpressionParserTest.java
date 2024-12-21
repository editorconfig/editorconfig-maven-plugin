package io.mpolivaha.maven.plugin.editorconfig.glob;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GlobExpressionParserTest {

  @ParameterizedTest
  @MethodSource("testAcceptsSource")
  void testAccepts(Path target, Path editrorconfig, String globPattern, boolean expectedAccepts) {
    boolean actualAccepts = new GlobExpressionParser(editrorconfig.toFile().getAbsolutePath()).accepts(target, globPattern);

    Assertions.assertThat(actualAccepts).isEqualTo(expectedAccepts);
  }

  static Stream<Arguments> testAcceptsSource() {
    return Stream.of(
      starCases()
    ).flatMap(Function.identity());
  }

  static Stream<Arguments> starCases() {
    return Stream.of(
        Arguments.of(
            Path.of("/some/dir/on/disk/Some.java"),
            Path.of("/some/dir/on/.editorconfig"),
            "*",
            false
        ),

        Arguments.of(
            Path.of("/some/dir/on/noextension"),
            Path.of("/some/dir/on/.editorconfig"),
            "*",
            true
        ),

        Arguments.of(
            Path.of("/some/dir/on/.gitconfig"),
            Path.of("/some/dir/on/.editorconfig"),
            "*",
            true
        ),

        Arguments.of(
            Path.of("/some/dir/on/Some.java"),
            Path.of("/some/dir/on/.editorconfig"),
            "*.java",
            true
        )
    );
  }
}