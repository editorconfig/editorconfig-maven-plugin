package io.mpolivaha.maven.plugin.editorconfig.model;

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
