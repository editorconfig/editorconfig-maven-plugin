package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;

public class ByteArrayLine {

  private final byte[] line;
  private final int eolStartsIndex;
  private final EndOfLine endOfLine;

  public ByteArrayLine(byte[] line, int eolStartsIndex, EndOfLine endOfLine) {
    // TODO: add assertions for parameters not being null
    this.line = line;
    this.eolStartsIndex = eolStartsIndex;
    this.endOfLine = endOfLine;
  }

  public int length() {
    return eolStartsIndex;
  }

  public EndOfLine getEndOfLine() {
    return endOfLine;
  }

  public boolean isTheLastLine() {
    return this.endOfLine.equals(EndOfLine.EOF);
  }
}
