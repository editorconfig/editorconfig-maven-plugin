/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.util.Optional;

public enum IndentationStyle {
    TAB('\t'),
    SPACE(' ');

    private final char encoding;

    public static Optional<IndentationStyle> from(String value) {
        for (IndentationStyle indentationStyle : values()) {
            if (indentationStyle.name().equalsIgnoreCase(value)) {
                return Optional.of(indentationStyle);
            }
        }
        return Optional.empty();
    }

    IndentationStyle(char c) {
        this.encoding = c;
    }

    public char getEncoding() {
        return encoding;
    }
}
