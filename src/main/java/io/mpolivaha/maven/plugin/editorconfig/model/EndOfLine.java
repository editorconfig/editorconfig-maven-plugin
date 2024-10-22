package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Optional;

public enum EndOfLine {

  CARRIAGE_RERUN("\r", "cr"),
  CARRIAGE_RERUN_LINE_FEED("\r\n", "crlf"),
  LINE_FEED("\n", "lf");

  private final String eolSymbol;
  private final String specMarker;

  EndOfLine(String eolSymbol, String specMarker) {
    this.eolSymbol = eolSymbol;
    this.specMarker = specMarker;
  }

  public static Optional<EndOfLine> from(String symbol) {
    for (EndOfLine value : values()) {
      if (value.specMarker.equals(symbol)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  public String getEolSymbol() {
    return eolSymbol;
  }
}
