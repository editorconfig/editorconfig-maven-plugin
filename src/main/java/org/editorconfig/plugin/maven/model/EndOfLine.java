/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.util.List;
import java.util.Optional;

public enum EndOfLine {
    CARRIAGE_RERUN("\r", "cr"),
    CARRIAGE_RERUN_LINE_FEED("\r\n", "crlf"),
    LINE_FEED("\n", "lf"),

    EOF("\0", null);

    private final String eolSymbol;
    private final String specMarker;

    EndOfLine(String eolSymbol, String specMarker) {
        this.eolSymbol = eolSymbol;
        this.specMarker = specMarker;
    }

    public static Optional<EndOfLine> from(String symbol) {
        for (EndOfLine value : List.of(CARRIAGE_RERUN, CARRIAGE_RERUN_LINE_FEED, LINE_FEED)) {
            if (value.specMarker.equals(symbol)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public String getPrintableSpecMarker() {
        return this.compareTo(EndOfLine.EOF) != 0 ? specMarker : "\\0 (END_OF_FILE)";
    }

    public boolean isSingleCharacter() {
        return !this.equals(CARRIAGE_RERUN_LINE_FEED);
    }

    public String getEolSymbol() {
        return eolSymbol;
    }

    public int getLengthInBytes() {
        return eolSymbol.length();
    }
}
