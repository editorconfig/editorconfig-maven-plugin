package io.mpolivaha.maven.plugin.editorconfig.glob;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link GlobExpressionParser}
 *
 * @author Mikhail Polivakha
 */
class GlobExpressionParserTest {

  @ParameterizedTest
  @MethodSource("testAcceptsSource")
  void testAccepts(Path target, Path editrorconfig, String globPattern, boolean expectedAccepts) {
    boolean actualAccepts = new GlobExpressionParser(editrorconfig.toFile().getAbsolutePath()).accepts(target, globPattern);

    Assertions.assertThat(actualAccepts).isEqualTo(expectedAccepts);
  }

  static Stream<Arguments> testAcceptsSource() {
    return Stream.of(
        starCases(),
        doubleStar(),
        sourceFiles()
    ).flatMap(Function.identity());
  }

  static Stream<Arguments> sourceFiles() {
    return Stream.of(
        Arguments.of(
            Path.of("/first/second/third/fourth/Some.java"),
            Path.of("/first/second/third/.editorconfig"),
            "*.{java,kt}",
            true
        ),
        Arguments.of(
            Path.of("/first/second/third/fourth/Some.kt"),
            Path.of("/first/second/third/.editorconfig"),
            "*.{java,kt}",
            true
        ),
        Arguments.of(
            Path.of("/first/second/third/fourth/Some.js"),
            Path.of("/first/second/third/.editorconfig"),
            "*.{java,kt}",
            false
        )
    );
  }

  static Stream<Arguments> doubleStar() {
    return Stream.of(
        Arguments.of(
            Path.of("/some/dir/on/disk/Some.java"),
            Path.of("/some/dir/on/.editorconfig"),
            "**/*",
            true
        ),
        Arguments.of(
            Path.of("/some/dir/on/Some.java"),
            Path.of("/some/dir/on/.editorconfig"),
            "**/*",
            false
        ),
        Arguments.of(
            Path.of("/some/dir/on/disk/nested/deep/Some.js"),
            Path.of("/some/dir/on/.editorconfig"),
            "**/*",
            true
        ),
        Arguments.of(
            Path.of("/some/dir/wrong_dir/file"),
            Path.of("/some/dir/on/.editorconfig"),
            "**/*",
            false
        )
    );
  }

  static Stream<Arguments> starCases() {
    return Stream.of(
        Arguments.of(
            Path.of("/some/dir/on/disk/Some.java"),
            Path.of("/some/dir/on/.editorconfig"),
            "*",
            true
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
            "*",
            true
        )
    );
  }
}