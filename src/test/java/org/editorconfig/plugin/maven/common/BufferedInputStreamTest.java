/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.junit.jupiter.api.Test;

/**
 * TODO add integration tests with real files
 */
class BufferedInputStreamTest {

    @Test
    void test_readLine_LF() throws IOException {
        // given
        String firstRow = "first row\n";
        String secondRow = "second row\n";
        String thirdRow = "third row\n";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
    }

    @Test
    void test_readLine_LF_lastLineIsEOFEnded() throws IOException {
        // given
        String firstRow = "first row\n";
        String secondRow = "second row\n";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.LINE_FEED);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.EOF);
    }

    @Test
    void test_readLine_CRLF() throws IOException {
        // given
        String firstRow = "first row\r\n";
        String secondRow = "second row\r\n";
        String thirdRow = "third row\r\n";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
    }

    @Test
    void test_readLine_CRLF_lastRowEndWithEOF() throws IOException {
        // given
        String firstRow = "first row\r\n";
        String secondRow = "second row\r\n";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.EOF);
    }

    @Test
    void test_readLine_CR() throws IOException {
        // given
        String firstRow = "first row\r";
        String secondRow = "second row\r";
        String thirdRow = "third row\r";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN);
    }

    @Test
    void test_readLine_CR_lastRowEndWithEOF() throws IOException {
        // given
        String firstRow = "first row\r";
        String secondRow = "second row\r";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        ByteArrayLine first = bufferedInputStream.readLine();
        ByteArrayLine second = bufferedInputStream.readLine();
        ByteArrayLine third = bufferedInputStream.readLine();

        Assertions.assertThat(first.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN);
        Assertions.assertThat(second.getEndOfLine()).isEqualTo(EndOfLine.CARRIAGE_RERUN);
        Assertions.assertThat(third.getEndOfLine()).isEqualTo(EndOfLine.EOF);
    }

    private static ByteArrayInputStream testFromAsciiString(String asciiString) {
        return new ByteArrayInputStream(asciiString.getBytes(StandardCharsets.US_ASCII));
    }
}
