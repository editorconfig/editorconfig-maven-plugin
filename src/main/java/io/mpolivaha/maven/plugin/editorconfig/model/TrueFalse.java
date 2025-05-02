/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Optional;

public enum TrueFalse {
    TRUE("true"),
    FALSE("false"),
    UNSET("unset");

    private final String symbol;

    TrueFalse(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Optional<TrueFalse> from(String source) {
        for (TrueFalse value : values()) {
            if (value.symbol.equalsIgnoreCase(source)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
