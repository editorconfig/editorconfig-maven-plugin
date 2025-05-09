/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.parser;

import java.nio.file.Path;

import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.model.GlobExpression;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.SectionBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * This class represents the context of the parser. It is supposed to hold all the relevant
 * information required for the parser to do its job.
 * <p>
 * P.S: This class is intentionally not a java {@link Record record}, because it is by its nature mutable,
 * due to the fact that the components of the context do change once parser moves on. The COW is not that
 * great since each new line inside the .editorconfig file will cause context recreation.
 *
 * @author Mikhail Polivakha
 */
public class ParsingContext {

    /**
     * Current line that parser is pointing at
     */
    private String line;

    /**
     * The number of the line inside the file
     */
    private int lineNumber;

    /**
     * The current section builder that we're assembling
     */
    private @Nullable SectionBuilder sectionBuilder;

    /**
     * The .editorconfig file we're currently parsing
     */
    private final Editorconfig editorconfig;

    public ParsingContext(String line, Path editorConfigLocation) {
        this.line = line;
        this.lineNumber = 0;
        this.sectionBuilder = null;
        this.editorconfig = new Editorconfig(editorConfigLocation);
    }

    public void newline(String line) {
        this.lineNumber++;
        this.line = line;
    }

    public void startNewSection(GlobExpression expression) {
        completePreviousSection();
        sectionBuilder = new SectionBuilder(expression);
    }

    public @NonNull Editorconfig completeBuild() {
        completePreviousSection();
        return editorconfig;
    }

    private void completePreviousSection() {
        if (sectionBuilder != null) {
            Section section = sectionBuilder.completeSection();
            editorconfig.addSection(section);
        }
    }

    public void markAsRoot() {
        editorconfig.markAsRoot();
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public SectionBuilder getSectionBuilder() {
        return sectionBuilder;
    }
}
