/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.editorconfig.plugin.maven.glob.GlobExpressionParser;

/**
 * .editorconfig file representation
 *
 * @author Mikhail Polivakha
 */
public class Editorconfig {

    private boolean isRoot = false;

    private final Path location;

    /**
     * Sections here are stored in order in which they were parsed.
     * <p>
     * TODO: maybe we should make Section implemented Ordered?
     */
    private final List<Section> sections = new LinkedList<>();

    private final GlobExpressionParser globExpressionParser;

    public Editorconfig(Path location) {
        this.location = location;
        this.globExpressionParser =
                new GlobExpressionParser(location.toAbsolutePath().normalize().toString());
    }

    public Editorconfig addSection(Section section) {
        this.sections.add(section);
        return this;
    }

    public void markAsRoot() {
        this.isRoot = true;
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

    public Path getParentDir() {
        return location.getParent();
    }

    /**
     * Finds a target {@link Section} within the {@link Editorconfig} file that matches the given file.
     * Note, however, that there may be multiple sections matching the same file. In this case, the effective,
     * merged {@link Section} is returned.
     */
    public Optional<Section> findTargetSection(Path file) {
        List<Section> matched = new LinkedList<>();

        for (Section section : sections) {
            if (globExpressionParser.accepts(file, section.getGlobExpression().getRaw())) {
                matched.add(section);
            }
        }

        if (matched.isEmpty()) {
            return Optional.empty();
        }

        if (matched.size() == 1) {
            return Optional.ofNullable(sections.get(0));
        }

        Section effectiveSection = matched.get(0);

        for (int i = 1; i < matched.size(); i++) {
            Section current = matched.get(i);
            effectiveSection = effectiveSection.mergeWith(current);
        }

        return Optional.of(effectiveSection);
    }
}
