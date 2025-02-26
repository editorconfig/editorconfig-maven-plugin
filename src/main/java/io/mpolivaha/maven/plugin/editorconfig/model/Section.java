package io.mpolivaha.maven.plugin.editorconfig.model;

import java.nio.file.Path;

/**
 * Corresponds to a particular section of the .editorconfig file
 *
 * @author Mikhail Polivakha
 */
public class Section {

    /**
     * Location of .editorconfig file relative to the classpath root
     */
    private final String location;

    private final IndentationStyle indentationStyle;

    private final GlobExpression globExpression;

    private final EndOfLine endOfLine;

    private final Charset charset;

    private final Integer indentationSize;

    private final Integer tabWidth;

    private final TrueFalse trimTrailingWhitespace;

    private final TrueFalse insertFinalNewLine;

    public Section(String location, IndentationStyle indentationStyle, GlobExpression globExpression, EndOfLine endOfLine, Charset charset, TrueFalse trimTrailingWhitespace, TrueFalse insertFinalNewLine, Integer indentationSize,
      Integer tabWidth) {
        this.location = location;
        this.indentationStyle = indentationStyle;
        this.globExpression = globExpression;
        this.endOfLine = endOfLine;
        this.charset = charset;
        this.trimTrailingWhitespace = trimTrailingWhitespace;
        this.insertFinalNewLine = insertFinalNewLine;
        this.tabWidth = tabWidth;
        this.indentationSize = indentationSize;
    }

    public String getLocation() {
        return location;
    }

    public IndentationStyle getIndentationStyle() {
        return indentationStyle;
    }

    public GlobExpression getGlobExpression() {
        return globExpression;
    }

    public EndOfLine getEndOfLine() {
        return endOfLine;
    }

    public Charset getCharset() {
        return charset;
    }

    public TrueFalse getTrimTrailingWhitespace() {
        return trimTrailingWhitespace;
    }

    public TrueFalse getInsertFinalNewLine() {
        return insertFinalNewLine;
    }

    public boolean accepts(Path file) {
        return this.globExpression.accepts(file);
    }

    /**
     * @return the width of the tab
     * @implSpec for making sense out of why this code is the way it is look<a href="https://spec.editorconfig.org/index.html#supported-pairs">at the spec doc</a>
     */
    public Integer getTabWidth() {
        return tabWidth == null ? indentationSize : tabWidth;
    }

    public Integer getIndentationSize() {
        return indentationSize;
    }
}
