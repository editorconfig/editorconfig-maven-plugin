package io.mpolivaha.maven.plugin.editorconfig.model;

import java.nio.file.Path;
import java.util.function.Supplier;

import org.jspecify.annotations.NonNull;

/**
 * Corresponds to a particular section of the .editorconfig file
 *
 * @author Mikhail Polivakha
 */
public class Section {

    private final IndentationStyle indentationStyle;

    private final GlobExpression globExpression;

    private final EndOfLine endOfLine;

    private final Charset charset;

    private final Integer indentationSize;

    private final Integer tabWidth;

    private final TrueFalse trimTrailingWhitespace;

    private final TrueFalse insertFinalNewLine;

    public Section(IndentationStyle indentationStyle, GlobExpression globExpression, EndOfLine endOfLine, Charset charset, TrueFalse trimTrailingWhitespace, TrueFalse insertFinalNewLine, Integer indentationSize,
      Integer tabWidth) {
        this.indentationStyle = indentationStyle;
        this.globExpression = globExpression;
        this.endOfLine = endOfLine;
        this.charset = charset;
        this.trimTrailingWhitespace = trimTrailingWhitespace;
        this.insertFinalNewLine = insertFinalNewLine;
        this.tabWidth = tabWidth;
        this.indentationSize = indentationSize;
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
     * Creates a merged {@link Section} from this one and the passed. {@link Option Options} in
     * the past {@link Section} take precedence over this one.
     * <p>
     * TODO: can we do it better? Code generation like mapstruct can help, theoretically.
     */
    public Section mergeWith(@NonNull Section that) {
        return new Section(
          orElseGet(that.indentationStyle, this.indentationStyle),
          orElseGet(that.globExpression, this.globExpression),
          orElseGet(that.endOfLine, this.endOfLine),
          orElseGet(that.charset, this.charset),
          orElseGet(that.trimTrailingWhitespace, this.trimTrailingWhitespace),
          orElseGet(that.insertFinalNewLine, this.insertFinalNewLine),
          orElseGet(that.indentationSize, this.indentationSize),
          orElseGet(that.tabWidth, this.tabWidth)
        );
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

    private <T> T orElseGet(T first, T second) {
        return first == null ? first : second;
    }
}
