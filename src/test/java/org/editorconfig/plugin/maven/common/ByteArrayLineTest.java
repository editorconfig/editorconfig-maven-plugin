/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.nio.charset.Charset;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.editorconfig.plugin.maven.common.ByteArrayLineTestUtils.fromUtf16BEString;
import static org.editorconfig.plugin.maven.common.ByteArrayLineTestUtils.fromUtf8String;

/**
 * Unit tests for {@link ByteArrayLine}
 *
 * @author Mikhail Polivakha
 */
class ByteArrayLineTest {

    @Test
    void isTheLastLine_2LinesLfEof_FalseTrue() {
        // given
        ByteArrayLine first = new ByteArrayLine(
                new byte[] {'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
        ByteArrayLine second =
                new ByteArrayLine(new byte[] {'H', 'e', 'l', 'l', 'o'}, 5, EndOfLine.EOF);

        // when
        EndOfLine firstEndOfLine = first.getEndOfLine();
        EndOfLine secondEndOfLine = second.getEndOfLine();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(firstEndOfLine).isEqualTo(EndOfLine.LINE_FEED);
            softAssertions.assertThat(secondEndOfLine).isEqualTo(EndOfLine.EOF);
            softAssertions.assertThat(first.isTheLastLine()).isFalse();
            softAssertions.assertThat(second.isTheLastLine()).isTrue();
        });
    }

    @Test
    void getContentWithEol_3LinesLfCrlfEof_Ok() {
        // given
        ByteArrayLine first = new ByteArrayLine(
                new byte[] {'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
        ByteArrayLine second = new ByteArrayLine(
                new byte[] {'A', 'l', 'e', 'x', '\r', '\n', '\0', '\0', '\0'},
                4,
                EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        ByteArrayLine third = new ByteArrayLine(
                new byte[] {'B', 'B', 'B', '\0', '\0', '\0', '\0'}, 3, EndOfLine.EOF);

        // when
        byte[] firstContent = first.getContentWithEol();
        byte[] secondContent = second.getContentWithEol();
        byte[] thirdContent = third.getContentWithEol();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions
                    .assertThat(firstContent)
                    .hasSize(6)
                    .containsExactly('H', 'e', 'l', 'l', 'o', '\n');
            softAssertions
                    .assertThat(secondContent)
                    .hasSize(6)
                    .containsExactly('A', 'l', 'e', 'x', '\r', '\n');
            softAssertions.assertThat(thirdContent).hasSize(4).containsExactly('B', 'B', 'B', '\0');
        });
    }

    @Test
    void lengthWithEol_3LinesLfCrlfEof_Ok() {
        // given
        ByteArrayLine first = new ByteArrayLine(
                new byte[] {'H', 'e', 'l', 'l', 'o', '\n'}, 5, EndOfLine.LINE_FEED);
        ByteArrayLine second = new ByteArrayLine(
                new byte[] {'A', 'l', 'e', 'x', '\r', '\n', '\0', '\0', '\0'},
                4,
                EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        ByteArrayLine third = new ByteArrayLine(
                new byte[] {'B', 'B', 'B', '\0', '\0', '\0', '\0'}, 3, EndOfLine.EOF);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(first.lengthWithEoL()).isEqualTo(6);
            softAssertions.assertThat(second.lengthWithEoL()).isEqualTo(6);
            softAssertions.assertThat(third.lengthWithEoL()).isEqualTo(4);
        });
    }

    @ParameterizedTest
    @MethodSource("test_isEmpty")
    void isEmpty_ListOfStrings_Ok(String string, EndOfLine endOfLine, boolean expected) {
        // given
        ByteArrayLine line = new ByteArrayLine(
                string.getBytes(US_ASCII),
                string.length() - endOfLine.getLengthInBytes(),
                endOfLine);

        // when
        boolean actual = line.isEmpty();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("test_getIndent")
    void getIndent_ListOfStrings_Ok(ByteArrayLine line, Charset charset, int expected) {
        // when
        int actual = line.getIndent(2, charset);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> test_getIndent() {
        return Stream.of(
                // UTF-8
                Arguments.of(fromUtf8String("package a.b.c;\n"), UTF_8, 0),
                Arguments.of(fromUtf8String("  void doSmth() {\r"), UTF_8, 2),
                Arguments.of(fromUtf8String("\t\tvoid doAnotherThing(){\r\n"), UTF_8, 4),
                Arguments.of(fromUtf8String("    void doAnotherThing(){\r"), UTF_8, 4),
                Arguments.of(fromUtf8String("      s = new String(bytes, UTF_8);\r"), UTF_8, 6),

                // UTF-16 Big endian
                Arguments.of(fromUtf16BEString("package a.b.c;\n"), UTF_16BE, 0),
                Arguments.of(fromUtf16BEString("p\n"), UTF_16BE, 0),
                Arguments.of(fromUtf16BEString("  void doSmth() {\r"), UTF_16BE, 2),
                Arguments.of(fromUtf16BEString("\t\tvoid doAnotherThing(){\r\n"), UTF_16BE, 4),
                Arguments.of(fromUtf16BEString("    void doAnotherThing(){\r"), UTF_16BE, 4),
                Arguments.of(
                        fromUtf16BEString("      s = new String(bytes, UTF_8);\r"), UTF_16BE, 6));
    }

    static Stream<Arguments> test_isEmpty() {
        return Stream.of(
                Arguments.of("\n", EndOfLine.LINE_FEED, true),
                Arguments.of("\r", EndOfLine.CARRIAGE_RERUN, true),
                Arguments.of("\r\n", EndOfLine.CARRIAGE_RERUN_LINE_FEED, true),
                Arguments.of("public static void main() {\n", EndOfLine.LINE_FEED, false),
                Arguments.of("\0", EndOfLine.EOF, true),
                Arguments.of("  \0", EndOfLine.EOF, false));
    }
}
