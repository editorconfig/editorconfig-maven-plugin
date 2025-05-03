/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.utils;

import java.util.Optional;

import org.editorconfig.plugin.maven.assertions.Assert;

/**
 * Utils for parsing editorconfig files
 *
 * @author Mikhail Polivakha
 */
public class ParsingUtils {

    public static boolean isComment(String line) {
        Assert.notNull(line, "Line cannot be null");

        return line.charAt(0) == '#' || line.charAt(0) == ';';
    }

    public static boolean isSectionStart(String line) {
        Assert.notNull(line, "Line cannot be null");

        String trimmed = line.trim();
        return trimmed.startsWith("[") && trimmed.endsWith("]");
    }

    public static Optional<KeyValue> parseKeyValue(String line) {
        Assert.notNull(line, "Line cannot be null");

        String[] parts = line.trim().split("=");

        if (parts.length != 2) {
            return Optional.empty();
        }

        return Optional.of(new KeyValue(parts[0].trim(), parts[1].trim()));
    }

    public record KeyValue(String key, String value) {}
}
