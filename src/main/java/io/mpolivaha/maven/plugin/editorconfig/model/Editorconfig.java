/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.model;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * .editorconfig file representation
 *
 * @author Mikhail Polivakha
 */
public class Editorconfig {

    private boolean isRoot = false;

    private Path location;

    private List<Section> sections;

    public Editorconfig addSection(Section section) {
        if (this.sections == null) {
            this.sections = new LinkedList<>();
        }
        this.sections.add(section);
        return this;
    }

    public Editorconfig markAsRoot() {
        this.isRoot = true;
        return this;
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Path getLocation() {
        return location;
    }

    public Editorconfig setLocation(Path location) {
        this.location = location;
        return this;
    }

    public Optional<Section> findTargetSection(Path file) {
        return this.sections.stream().filter(section -> section.accepts(file)).findFirst();
    }

    /**
     * Builder for {@link Section section}.
     *
     * <p>
     * This class is <strong>intentionally</strong> not static, as it is, by design, is created
     * within the {@link Editorconfig}. There might be an option to just composite the {@link Editorconfig}
     * instance within {@link Section current section}, but the design decision was made to try to solve this
     * via java inner classes.
     */
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
         * Obtain the enclosing editorconfig
         */
        public Editorconfig getEditorconfig() {
            return Editorconfig.this;
        }

        /**
         * Build section withing {@link Editorconfig editorconfig}, that is the enclosing for this {@link SectionBuilder builder}.
         */
        public Editorconfig completeSection() {
            Editorconfig editorconfig = getEditorconfig();
            editorconfig.addSection(new Section(
                    indentationStyle,
                    globExpression,
                    endOfLine,
                    charset,
                    trimTrailingWhitespace,
                    insertFinalNewLine,
                    indentationSize,
                    tabWidth));
            return editorconfig;
        }
    }
}
