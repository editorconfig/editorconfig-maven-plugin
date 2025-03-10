package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link ByteArrayLine}
 *
 * @author Mikhail Polivakha
 */
class ByteArrayLineTest {

  @Test
  void test_enfOfLine() {

    // when.
    ByteArrayLine first = new ByteArrayLine(new byte[]{'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
    ByteArrayLine second = new ByteArrayLine(new byte[]{'H', 'e', 'l', 'l', 'o'}, 5, EndOfLine.EOF);

    // then.
    Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
    Assertions.assertThat(first.isTheLastLine()).isFalse();
    Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.EOF);
    Assertions.assertThat(second.isTheLastLine()).isTrue();
  }

  @Test
  void test_getContent() {

    // when.
    ByteArrayLine first = new ByteArrayLine(new byte[]{'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
    ByteArrayLine second = new ByteArrayLine(new byte[]{'A', 'l', 'e', 'x', '\r', '\n', '\0', '\0', '\0'}, 4, EndOfLine.CARRIAGE_RERUN_LINE_FEED);
    ByteArrayLine third = new ByteArrayLine(new byte[]{'B', 'B', 'B', '\0', '\0', '\0', '\0'}, 3, EndOfLine.EOF);

    // then.
    Assertions.assertThat(first.getContentWithEol()).hasSize(6).containsExactly('H', 'e', 'l', 'l', 'o', '\n');
    Assertions.assertThat(second.getContentWithEol()).hasSize(6).containsExactly('A', 'l', 'e', 'x', '\r', '\n');
    Assertions.assertThat(third.getContentWithEol()).hasSize(4).containsExactly('B', 'B', 'B', '\0');
  }

  @Test
  void test_lengthWithEol() {

    // when.
    ByteArrayLine first = new ByteArrayLine(new byte[]{'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
    ByteArrayLine second = new ByteArrayLine(new byte[]{'A', 'l', 'e', 'x', '\r', '\n', '\0', '\0', '\0'}, 4, EndOfLine.CARRIAGE_RERUN_LINE_FEED);
    ByteArrayLine third = new ByteArrayLine(new byte[]{'B', 'B', 'B', '\0', '\0', '\0', '\0'}, 3, EndOfLine.EOF);

    // then.
    Assertions.assertThat(first.lengthWithEoL()).isEqualTo(6);
    Assertions.assertThat(second.lengthWithEoL()).isEqualTo(6);
    Assertions.assertThat(third.lengthWithEoL()).isEqualTo(4);
  }

  @Test
  void test_getIndentInColumns_noIndent() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{'p', 'u', 'b', 'l', 'i', 'c', ' ', 'v', 'o', 'i', 'd', ' ', 's', 'a', 'v', 'e', '(', ')', '{', '\n'},
        5,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(2)).isEqualTo(0);
  }

  @Test
  void test_getIndentInColumns_emptyLine() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{'\n'},
        0,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(4)).isEqualTo(0);
  }

  @Test
  void test_getIndentInColumns_spaceThenEndOfLine() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{' ', ' ', '\n'},
        2,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(4)).isEqualTo(2);
  }

  @Test
  void test_getIndentInColumns_indentedOnlyWithSpaced() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{' ', ' ', ' ', ' ', 'p', 'u', 'b', 'l', 'i', 'c', ' ', 'v', 'o', 'i', 'd', ' ', 's', 'a', 'v', 'e', '(', ')', '{', '\n'},
        5,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(0)).isEqualTo(4);
  }

  @Test
  void test_getIndentInColumns_indentedOnlyWithHardTabs() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{'\t', '\t', 'p', 'u', 'b', 'l', 'i', 'c', ' ', 'v', 'o', 'i', 'd', ' ', 's', 'a', 'v', 'e', '(', ')', '{', '\n'},
        5,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(6)).isEqualTo(12);
  }

  @Test
  void test_getIndentInColumns_mixedIndentation() {

    // when.
    ByteArrayLine sut = new ByteArrayLine(
        new byte[]{'\t', '\t', ' ', ' ', 'p', 'u', 'b', 'l', 'i', 'c', ' ', 'v', 'o', 'i', 'd', ' ', 's', 'a', 'v', 'e', '(', ')', '{', '\n'},
        5,
        EndOfLine.LINE_FEED
    );

    // then.
    Assertions.assertThat(sut.getIndentInColumns(2)).isEqualTo(6);
  }

  @ParameterizedTest
  @MethodSource("test_startsNewCodeBlock_args")
  void test_startsNewCodeBlock(String string, boolean expectedStartsNewCodeBlock) {

    // when.
    boolean startsNewCodeBlock = new ByteArrayLine(
        string.getBytes(StandardCharsets.US_ASCII),
        string.length() - 1,
        EndOfLine.LINE_FEED
    ).startsNewCodeBlock();

    // then.
    Assertions.assertThat(startsNewCodeBlock).isEqualTo(expectedStartsNewCodeBlock);
  }

  @ParameterizedTest
  @MethodSource("test_isEmpty")
  void test_isEmpty(String string, EndOfLine endOfLine, boolean expectedIsEmpty) {

    // when.
    boolean actualIsEmpty = new ByteArrayLine(
        string.getBytes(StandardCharsets.US_ASCII),
        string.length() - endOfLine.getLengthInBytes(),
        endOfLine
    ).isEmpty();

    // then.
    Assertions.assertThat(actualIsEmpty).isEqualTo(expectedIsEmpty);
  }

  static Stream<Arguments> test_isEmpty() {
    return Stream.of(
        Arguments.of("\n", EndOfLine.LINE_FEED, true),
        Arguments.of("\r", EndOfLine.CARRIAGE_RERUN, true),
        Arguments.of("\r\n", EndOfLine.CARRIAGE_RERUN_LINE_FEED, true),
        Arguments.of("public static void main() {\n", EndOfLine.LINE_FEED, false),
        Arguments.of("\0", EndOfLine.EOF, true),
        Arguments.of("  \0", EndOfLine.EOF, false)
    );
  }

  static Stream<Arguments> test_startsNewCodeBlock_args() {
    return Stream.of(
      Arguments.of("package com.example;\n", false),
      Arguments.of("public static void main() {\n", true),
      Arguments.of("public static void main() { \n", true),
      Arguments.of("((CastToSomething) other).invoke (\n", true),
      Arguments.of("if (something()) \n", false),
      Arguments.of("[\n", true),
      Arguments.of("[1, 2, 3].length", false),
      Arguments.of("if (something()) \n", false),
      Arguments.of("if (something()) {\n", true)
    );
  }
}