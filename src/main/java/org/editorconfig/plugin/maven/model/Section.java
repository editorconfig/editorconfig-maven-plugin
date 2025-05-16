/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.editorconfig.plugin.maven.annotations.Immutable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Corresponds to a particular section of the .editorconfig file
 *
 * @author Mikhail Polivakha
 */
@Immutable
public class Section {

    private final @NonNull OptionValue<IndentationStyle> indentationStyle;

    private final @NonNull GlobExpression globExpression;

    private final @NonNull OptionValue<EndOfLine> endOfLine;

    private final @NonNull OptionValue<Charset> charset;

    private final @NonNull OptionValue<Integer> indentationSize;

    private final @NonNull OptionValue<Integer> tabWidth;

    private final @NonNull OptionValue<TrueFalse> trimTrailingWhitespace;

    private final @NonNull OptionValue<TrueFalse> insertFinalNewLine;

    public Section(
            OptionValue<IndentationStyle> indentationStyle,
            GlobExpression globExpression,
            OptionValue<EndOfLine> endOfLine,
            OptionValue<Charset> charset,
            OptionValue<TrueFalse> trimTrailingWhitespace,
            OptionValue<TrueFalse> insertFinalNewLine,
            OptionValue<Integer> indentationSize,
            OptionValue<Integer> tabWidth) {
        this.indentationStyle = orElseNull(indentationStyle);
        this.globExpression = globExpression;
        this.endOfLine = orElseNull(endOfLine);
        this.charset = orElseNull(charset);
        this.trimTrailingWhitespace = orElseNull(trimTrailingWhitespace);
        this.insertFinalNewLine = orElseNull(insertFinalNewLine);
        this.tabWidth = orElseNull(tabWidth);
        this.indentationSize = orElseNull(indentationSize);
    }

    public OptionValue<IndentationStyle> getIndentationStyle() {
        return indentationStyle;
    }

    public GlobExpression getGlobExpression() {
        return globExpression;
    }

    public OptionValue<EndOfLine> getEndOfLine() {
        return endOfLine;
    }

    public OptionValue<Charset> getCharset() {
        return charset;
    }

    public OptionValue<TrueFalse> getTrimTrailingWhitespace() {
        return trimTrailingWhitespace;
    }

    public OptionValue<TrueFalse> getInsertFinalNewLine() {
        return insertFinalNewLine;
    }

    /**
     * Creates a merged {@link Section} from this one and the passed. {@link Option Options} in
     * the provided {@link Section} take precedence over {@code this} one.
     */
    public Section mergeWith(@NonNull Section that) {
        return new Section(
                mergeValues(that.indentationStyle, this.indentationStyle),
                that.globExpression, // TODO: this is not right...
                mergeValues(that.endOfLine, this.endOfLine),
                mergeValues(that.charset, this.charset),
                mergeValues(that.trimTrailingWhitespace, this.trimTrailingWhitespace),
                mergeValues(that.insertFinalNewLine, this.insertFinalNewLine),
                mergeValues(that.indentationSize, this.indentationSize),
                mergeValues(that.tabWidth, this.tabWidth));
    }

    /**
     * @return the width of the tab, or null if it is either explicitly marked as {@code unset}, or is not set at all.
     * @implSpec for making sense out of why this code is the way it is look<a href="https://spec.editorconfig.org/index.html#supported-pairs">at the spec doc</a>
     */
    public @Nullable Integer getTabWidthAsDigit() {
        if (tabWidth.isUnset()) {
            return null;
        }

        if (tabWidth.getValue() != null) {
            return tabWidth.getValue();
        }

        return getIndentationSizeAsDigit();
    }

    public Integer getIndentationSizeAsDigit() {
        if (indentationSize.isUnset()) {
            return null;
        }

        if (indentationSize.getValue() != null) {
            return indentationSize.getValue();
        }

        return null;
    }

    public OptionValue<Integer> getIndentationSize() {
        return indentationSize;
    }

    private <T> OptionValue<T> mergeValues(OptionValue<T> first, OptionValue<T> second) {
        // the value was explicitly unset
        if (first.isUnset()) {
            return OptionValue.unset();
        }

        if (first.getValue() == null) {
            // either not present or was not parsed
            return second;
        } else {
            // value was explicitly set
            return first;
        }
    }

    private <T> OptionValue<T> orElseNull(OptionValue<T> optionValue) {
        if (optionValue == null) {
            return OptionValue.fromValue(null);
        } else {
            return optionValue;
        }
    }
}
