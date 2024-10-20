package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Optional;

public enum EndOfLine {

  CARRIAGE_RERUN("\r"),
  CARRIAGE_RERUN_LINE_FEED("\r\n"),
  LINE_FEED("\n");

  private final String eolSymbol;

  EndOfLine(String eolSymbol) {
    this.eolSymbol = eolSymbol;
  }

  public static Optional<EndOfLine> from(String symbol) {
    for (EndOfLine value : values()) {
      if (value.eolSymbol.equals(symbol)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  public String getEolSymbol() {
    return eolSymbol;
  }
}
