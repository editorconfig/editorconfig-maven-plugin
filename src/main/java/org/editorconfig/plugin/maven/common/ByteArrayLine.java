/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.model.EndOfLine;

/**
 * Abstraction of the line of source code being analyzed. It is backed by the {@code byte[]} in
 * order to have more control over the interpretation of those bytes on the parsing side.
 *
 * @author Mikhail Polivakha
 */
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
        Assert.notNull(line, "The line cannot be null");
        Assert.notNull(eolStartsIndex, "The line cannot be null");
        Assert.notNull(endOfLine, "The line cannot be null");

        this.line = line;
        this.eolStartsIndex = eolStartsIndex;
        this.endOfLine = endOfLine;
    }

    public EndOfLine getEndOfLine() {
        return endOfLine;
    }

    /**
     * @return true in case this {@link ByteArrayLine} represents the last line
     *         of the file that contains the EOF (-1) symbol only
     */
    public boolean isLastEmptyEOFLine() {
        return isTheLastLine() && getContentWithEol().length == 1;
    }

    public boolean isEmpty() {
        return isLastEmptyEOFLine() || (lengthWithEoL() - endOfLine.getLengthInBytes() == 0);
    }

    public boolean isTheLastLine() {
        return this.endOfLine.equals(EndOfLine.EOF);
    }

    public byte[] getContentWithEol() {
        return Arrays.copyOf(line, getEolStartsIndex() + endOfLine.getLengthInBytes());
    }

    public byte at(int i) {
        Assert.state(
                () -> i >= 0 && i < lengthWithEoL(),
                "Incorrect index is provided for ByteArrayLine");
        return line[i];
    }

    public int getEolStartsIndex() {
        return eolStartsIndex;
    }

    /**
     * @return the length of the read line <strong>INCLUDING the end of line symbol/symbols</strong>
     */
    public int lengthWithEoL() {
        return getEolStartsIndex() + endOfLine.getLengthInBytes();
    }

    /**
     * Get indentation size of the line in abstract "columns". An assumption is that the 'tab' ('\t' ASCII symbol)
     * can be broken down into multiple of such columns, and each space used for indentation has size of an exactly
     * one column.
     * <p>
     * The {@link Charset} is an absolute necessity here, since the line that starts with two spaces must always
     * represent the two columns indentation from the point of view of the user. However, these two spaces may
     * occupy 2 bytes in case of UTF-8, and 4 bytes in case of UTF-16.
     *
     * @param softTabsForOneHard amount of "columns" or "spaces" or "soft tabs" in one tab (also called hard tab)
     * @param charset charset for interpretation of the line string.
     */
    public int getIndent(int softTabsForOneHard, Charset charset) {
        if (isEmpty()) {
            return 0;
        }

        var charsetAwareLine = new String(line, charset);
        int index = 0;
        int countOfChars = 0;
        char currentChar;
        boolean isWhitespace;

        do {
            currentChar = charsetAwareLine.charAt(index++);
            isWhitespace = Character.isWhitespace(currentChar);
            if (isWhitespace) {
                if (currentChar == '\t') {
                    countOfChars += softTabsForOneHard;
                } else {
                    countOfChars++;
                }
            }
        } while (isWhitespace && index < charsetAwareLine.length());

        return countOfChars;
    }
}
