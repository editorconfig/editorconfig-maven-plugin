/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import java.nio.file.Path;

/**
 * Glob expression of the particular {@link Section}.
 *
 * @author Mikhail Polivakha
 */
public class GlobExpression {

    private final String raw;

    private GlobExpression(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

    public static GlobExpression from(String raw) {
        return new GlobExpression(raw);
    }

    public boolean accepts(Path path) {
        throw new UnsupportedOperationException();
    }
}
