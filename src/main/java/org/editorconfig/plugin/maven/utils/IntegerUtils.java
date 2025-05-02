/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.utils;

import java.util.Optional;

public class IntegerUtils {

    public static Optional<Integer> parseIntSafe(String integer) {
        try {
            return Optional.of(Integer.valueOf(integer));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
