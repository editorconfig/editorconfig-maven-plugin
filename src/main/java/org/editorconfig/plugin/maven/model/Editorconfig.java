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

    private Path location;

    private List<Section> sections = new LinkedList<>();

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

    public Optional<Section> findTargetSection(Path file) {
        return this.sections.stream()
                .filter(section -> globExpressionParser.accepts(
                        file, section.getGlobExpression().getRaw()))
                .findFirst();
    }
}
