/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.model;

import org.jspecify.annotations.NonNull;

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

    public static GlobExpression from(@NonNull String raw) {
        if (raw.startsWith("[") && raw.endsWith("]")) {
            return new GlobExpression(raw.substring(1, raw.length() - 1));
        } else {
            return new GlobExpression(raw);
        }
    }
}
