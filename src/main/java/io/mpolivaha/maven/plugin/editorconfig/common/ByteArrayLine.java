package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ByteArrayLine {

  private final byte[] line;
  private final int eolStartsIndex;
  private final EndOfLine endOfLine;


  /**
   * @param line the line itself. Includes the end of lien character(-s)
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

  public byte[] getContent() {
    return Arrays.copyOf(line, getEolStartsIndex() + 1);
  }

  public byte at(int i) {
    // TODO: assert the i borders
    return line[i];
  }

  public int getEolStartsIndex() {
    return eolStartsIndex;
  }

  /**
   * @return the length of the read line <strong>WITHOUT taking into account the end of line symbol/symbols</strong>
   */
  public int length() {
    return getEolStartsIndex();
  }

  /**
   * @return the length of the read line <strong>INCLUDING the end of line symbol/symbols</strong>
   */
  public int lengthWithEoL() {
    return getEolStartsIndex() + endOfLine.getLengthInBytes();
  }

  /**
   * Returns the indentation in columns. One space is one column. One tab has length in column tact is equal
   * to the {@link Section#getTabWidth() tab_width} option.
   *
   * @param softTabsForOneHard how many columns does one tab represents
   */
  public int getIndentInColumns(Integer softTabsForOneHard) {
    ByteArrayLine flatten = flatten(softTabsForOneHard);
    for (int i = 0; i < flatten.lengthWithEoL(); i++) {
      if (flatten.at(i) != ' ') {
        return i + 1;
      }
    }
    return flatten.lengthWithEoL();
  }

  /**
   * Flattens the line, which means that it replaces all the hard tabs (\t) with
   * the corresponding amount of the soft tabs
   *
   * @param softTabsForOneHard - Amount of soft tabs per one hard tab
   */
  public ByteArrayLine flatten(Integer softTabsForOneHard) {
    int initialLength = lengthWithEoL();
    var newLine = new ArrayList<Byte>(initialLength);
    int i = 0;
    while (i < initialLength) {
      byte currentByte = at(i);
      if (currentByte != ' ' && currentByte != '\t') {
        // we met first significant byte, newline, or EOL.
        // Here, the end of line symbol is copied as well since it is included into the lengthWithEoL()
        // TODO: there is no straightforward way of converting the array of bytes into a list
        for (byte b : Arrays.copyOfRange(line, i, initialLength)) {
          newLine.add(b);
        }
        break;
      }
      if (currentByte != '\t') {
        for (int j = 0; j < softTabsForOneHard; j++) {
          newLine.add((byte) ' ');
        }
      } else {
        newLine.add(currentByte);
      }
    }
    return new ByteArrayLine(toPrimitive(newLine.toArray(new Byte[0])), newLine.size(), endOfLine);
  }

  private static byte[] toPrimitive(Byte[] array) {
    byte[] newArr = new byte[array.length];
    for (int i = 0; i < array.length; i++) {
      newArr[i] = array[i];
    }
    return newArr;
  }
}
