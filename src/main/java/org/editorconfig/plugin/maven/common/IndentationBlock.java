/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

/**
 * @param indentLevel level of indentation is used for this block
 */
public record IndentationBlock(int indentLevel) {

    public static IndentationBlock root() {
        return new IndentationBlock(0);
    }

    public IndentationBlock next(int indentSize) {
        return new IndentationBlock(this.indentLevel + indentSize);
    }
}
