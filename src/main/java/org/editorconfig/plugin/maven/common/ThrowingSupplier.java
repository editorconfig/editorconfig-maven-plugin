/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

    T get() throws E;
}
