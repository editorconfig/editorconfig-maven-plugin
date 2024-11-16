package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;

public class ByteArrayLine {

  private final byte[] line;
  private final int eolStartsIndex;
  private final EndOfLine endOfLine;

  /**
   * @param line the line itself
   * @param eolStartsIndex the index where eol starts. In other words, if eol is LF (\n),
   *                       this param would have the index of the \n, not of the previous
   *                       element
   * @param endOfLine the end of line determined by the parser
   */
  public ByteArrayLine(byte[] line, int eolStartsIndex, EndOfLine endOfLine) {
    // TODO: add assertions for parameters not being null
    this.line = line;
    this.eolStartsIndex = eolStartsIndex;
    this.endOfLine = endOfLine;
  }

  public EndOfLine getEndOfLine() {
    return endOfLine;
  }

  public boolean isTheLastLine() {
    return this.endOfLine.equals(EndOfLine.EOF);
  }

  public byte at(int i) {
    // TODO: assert the i borders
    return line[i];
  }

  public int getEolStartsIndex() {
    return eolStartsIndex;
  }
}
