/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.editorconfig.plugin.maven.model.EndOfLine;

/**
 * Utilities for easier instantiation of {@link ByteArrayLine} in tests.
 *
 * @author Mikhail Polivakha
 */
public class ByteArrayLineTestUtils {

    public static ByteArrayLine fromUtf8String(String s) {
        return fromString(s, StandardCharsets.UTF_8);
    }

    public static ByteArrayLine fromUtf16BEString(String s) {
        return fromString(s, StandardCharsets.UTF_16BE);
    }

    private static ByteArrayLine fromString(String s, Charset charset) {
        int crlf = s.indexOf("\r\n");
        int cr = s.indexOf("\r");
        int lf = s.indexOf("\n");

        if (crlf > 0) {
            return new ByteArrayLine(s.getBytes(charset), crlf, EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        }
        if (cr > 0) {
            return new ByteArrayLine(s.getBytes(charset), cr, EndOfLine.CARRIAGE_RERUN);
        }
        if (lf > 0) {
            return new ByteArrayLine(s.getBytes(charset), lf, EndOfLine.CARRIAGE_RERUN_LINE_FEED);
        }

        throw new RuntimeException("Cannot infer the EOL symbol for string : " + s);
    }
}
