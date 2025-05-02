/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

/**
 * Runnable that can throw any {@link Throwable}
 *
 * @author Mikhail Polivakha
 */
@FunctionalInterface
public interface ThrowingRunnable<T, E extends Throwable> {

    void run() throws E;
}
