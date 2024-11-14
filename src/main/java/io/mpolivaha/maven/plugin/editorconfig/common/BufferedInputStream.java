package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link InputStream} that is capable to read lines, but at the same
 * time it is reading the line as raw bytes avoiding conversion into {@link String}
 *
 * @author Mikhail Polivakha
 */
public class BufferedInputStream extends FilterInputStream {

  public static final Integer DEFAULT_LINE_LENGTH = 256;
  private static final int EOF = -1;

  public BufferedInputStream(InputStream inputStream) {
    super(inputStream);
  }

  /**
   * Well... The huge part of the problem is that there is no built-in way
   * in any language in the galaxy to know the line terminating character for
   * the given sequence of bytes. Yes, we have {@link System#lineSeparator()},
   * but this is not going to help. It represents the standard line separator
   * for the text files on the given OS. The particular file we're parsing might
   * be different.
   * <p>
   * For instance, if we have {@link Option#END_OF_LINE} configured for the value
   * {@link EndOfLine#LINE_FEED}, but we're opening this file on Windows - we simply
   * cannot count of {@link System#lineSeparator()} - it would not work.
   *
   * @return ByteArrayLine as a wrapper over the byte array fo the ease of use.
   * The problem with byte array is that it may and typically
   * would contain training {@link #EOF nulls}. To ease the use on the callee side,
   * we're making this wrapper.
   */
  public ByteArrayLine readLine() throws IOException {
      byte[] line = new byte[DEFAULT_LINE_LENGTH];
      int index = 0;
      int next;
      EndOfLine determinedEol = null;

      while ((next = read()) != -1) {
        line = ensureLineCapacity(line, index);

        byte b = ((byte) next);
        line[index++] = b;
        if (b == '\n') { //newline on UNIX-like/linux/BSD
          determinedEol = EndOfLine.LINE_FEED;
          break;
        }
        if (b == '\r') {
          int upcoming = read();

          // here, we're checking if the line ends with CRLF
          // if it is not, we're making an assumption that
          // we are on macOS, and CR is the line separator.
          // It of course might not be true, and we may be on
          // Windows with CRLF, but the assumption is that even
          // if we're on Windows and end of line is really CRLF,
          // we're not using CR inside the string randomly on its own
          if (upcoming == '\n') {
            ensureLineCapacity(line, index);
            line[index++] = (byte) upcoming;
            determinedEol = EndOfLine.CARRIAGE_RERUN_LINE_FEED;
            break;
          }
          determinedEol = EndOfLine.CARRIAGE_RERUN;
          break;
        }
      }

      if (next == EOF) {
        line = ensureLineCapacity(line, index);
        line[index] = EOF;
      }

      // The absence of the EOL means we've read the file till the end
      // and the last line does not end with the EOL symbol. It is of course
      // does not comply with the POSIX, but we're going to forgive the use
      // for that (in fact we have to, we've an Option for that)
      if (determinedEol == null) {
        determinedEol = EndOfLine.EOF;
      }
      return new ByteArrayLine(line, determinedEol.isSingleCharacter() ? index - 1 : index - 2, determinedEol);
  }

  private byte[] ensureLineCapacity(byte[] line, int index) {
    if (line.length >= index) {
      byte[] dest = new byte[line.length * 2];
      System.arraycopy(line, 0, dest, 0, line.length);
      return dest;
    }
    return line;
  }
}
