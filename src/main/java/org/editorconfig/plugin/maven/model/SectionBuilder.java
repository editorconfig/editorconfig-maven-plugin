/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

public class SectionBuilder {

    private final GlobExpression globExpression;
    private OptionValue<IndentationStyle> indentationStyle;
    private OptionValue<Integer> indentationSize;
    private OptionValue<Integer> tabWidth;
    private OptionValue<EndOfLine> endOfLine;
    private OptionValue<Charset> charset;
    private OptionValue<TrueFalse> trimTrailingWhitespace;
    private OptionValue<TrueFalse> insertFinalNewLine;

    public SectionBuilder(GlobExpression globExpression) {
        this.globExpression = globExpression;
    }

    public SectionBuilder indentationStyle(OptionValue<IndentationStyle> indentationStyle) {
        this.indentationStyle = indentationStyle;
        return this;
    }

    public SectionBuilder indentationSize(OptionValue<Integer> indentationSize) {
        this.indentationSize = indentationSize;
        return this;
    }

    public SectionBuilder tabWidth(OptionValue<Integer> tabWidth) {
        this.tabWidth = tabWidth;
        return this;
    }

    public SectionBuilder endOfLine(OptionValue<EndOfLine> endOfLine) {
        this.endOfLine = endOfLine;
        return this;
    }

    public SectionBuilder charset(OptionValue<Charset> charset) {
        this.charset = charset;
        return this;
    }

    public SectionBuilder trimTrailingWhitespace(OptionValue<TrueFalse> trimTrailingWhitespace) {
        this.trimTrailingWhitespace = trimTrailingWhitespace;
        return this;
    }

    public SectionBuilder insertFinalNewLine(OptionValue<TrueFalse> insertFinalNewLine) {
        this.insertFinalNewLine = insertFinalNewLine;
        return this;
    }

    public GlobExpression getGlobExpression() {
        return this.globExpression;
    }

    /**
     * Build section withing {@link Editorconfig editorconfig}, that is the enclosing for this {@link SectionBuilder builder}.
     */
    public Section completeSection() {
        return new Section(
                indentationStyle,
                globExpression,
                endOfLine,
                charset,
                trimTrailingWhitespace,
                insertFinalNewLine,
                indentationSize,
                tabWidth);
    }
}
