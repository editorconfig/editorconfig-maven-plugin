/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.common;

/**
 * Runnable that can throw any {@link Throwable}
 *
 * @author Mikhail Polivakha
 */
@FunctionalInterface
public interface ThrowingRunnable<T, E extends Throwable> {

    void run() throws E;
}
