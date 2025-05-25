/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.editorconfig.plugin.maven.model.EndOfLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO add integration tests with real files
 */
class BufferedInputStreamTest {

    @Test
    void getEndOfLine_3LinesLfLfLf_Lf() throws IOException {
        // given
        String firstRow = "first row\n";
        String secondRow = "second row\n";
        String thirdRow = "third row\n";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.LINE_FEED, firstEndOfLine);
        assertEquals(EndOfLine.LINE_FEED, secondEndOfLine);
        assertEquals(EndOfLine.LINE_FEED, thirdEndOfLine);
    }

    @Test
    void getEndOfLine_3LinesLfLfEof_Eof() throws IOException {
        // given
        String firstRow = "first row\n";
        String secondRow = "second row\n";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.LINE_FEED, firstEndOfLine);
        assertEquals(EndOfLine.LINE_FEED, secondEndOfLine);
        assertEquals(EndOfLine.EOF, thirdEndOfLine);
    }

    @Test
    void getEndOfLine_3LinesCrlfCrlfCrlf_Crlf() throws IOException {
        // given
        String firstRow = "first row\r\n";
        String secondRow = "second row\r\n";
        String thirdRow = "third row\r\n";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.CARRIAGE_RERUN_LINE_FEED, firstEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN_LINE_FEED, secondEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN_LINE_FEED, thirdEndOfLine);
    }

    @Test
    void getEndOfLine_3LinesCrlfCrlfEof_Eof() throws IOException {
        // given
        String firstRow = "first row\r\n";
        String secondRow = "second row\r\n";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.CARRIAGE_RERUN_LINE_FEED, firstEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN_LINE_FEED, secondEndOfLine);
        assertEquals(EndOfLine.EOF, thirdEndOfLine);
    }

    @Test
    void getEndOfLine_3LinesCrCrCr_Cr() throws IOException {
        // given
        String firstRow = "first row\r";
        String secondRow = "second row\r";
        String thirdRow = "third row\r";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.CARRIAGE_RERUN, firstEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN, secondEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN, thirdEndOfLine);
    }

    @Test
    void getEndOfLine_3LinesCrCrEof_Eof() throws IOException {
        // given
        String firstRow = "first row\r";
        String secondRow = "second row\r";
        String thirdRow = "third row";

        String source = firstRow + secondRow + thirdRow;

        BufferedInputStream bufferedInputStream =
                new BufferedInputStream(testFromAsciiString(source));

        // when
        EndOfLine firstEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine secondEndOfLine = bufferedInputStream.readLine().getEndOfLine();
        EndOfLine thirdEndOfLine = bufferedInputStream.readLine().getEndOfLine();

        // then
        assertEquals(EndOfLine.CARRIAGE_RERUN, firstEndOfLine);
        assertEquals(EndOfLine.CARRIAGE_RERUN, secondEndOfLine);
        assertEquals(EndOfLine.EOF, thirdEndOfLine);
    }

    private static ByteArrayInputStream testFromAsciiString(String asciiString) {
        return new ByteArrayInputStream(asciiString.getBytes(StandardCharsets.US_ASCII));
    }
}
