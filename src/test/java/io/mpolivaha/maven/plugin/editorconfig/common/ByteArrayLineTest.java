package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
}