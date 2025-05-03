/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

public class SectionBuilder {

    private IndentationStyle indentationStyle;
    private Integer indentationSize;
    private Integer tabWidth;
    private final GlobExpression globExpression;
    private EndOfLine endOfLine;
    private Charset charset;
    private TrueFalse trimTrailingWhitespace;

    private TrueFalse insertFinalNewLine;

    public SectionBuilder(GlobExpression globExpression) {
        this.globExpression = globExpression;
    }

    public SectionBuilder indentationStyle(IndentationStyle indentationStyle) {
        this.indentationStyle = indentationStyle;
        return this;
    }

    public SectionBuilder indentationSize(Integer indentationSize) {
        this.indentationSize = indentationSize;
        return this;
    }

    public SectionBuilder tabWidth(Integer tabWidth) {
        this.tabWidth = tabWidth;
        return this;
    }

    public SectionBuilder endOfLine(EndOfLine endOfLine) {
        this.endOfLine = endOfLine;
        return this;
    }

    public SectionBuilder charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public SectionBuilder trimTrailingWhitespace(TrueFalse trimTrailingWhitespace) {
        this.trimTrailingWhitespace = trimTrailingWhitespace;
        return this;
    }

    public SectionBuilder insertFinalNewLine(TrueFalse insertFinalNewLine) {
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
