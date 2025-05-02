/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.assertions;

import java.util.function.Supplier;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Assertion class
 *
 * @author Mikhail Polivakha
 */
public class Assert {

    public static <T extends Throwable> void sneakyThrows(Throwable exception) throws T {
        throw (T) exception;
    }

    public static <T> void notNull(T element, String message) {
        if (element == null) {
            sneakyThrows(new MojoExecutionException(message));
        }
    }

    public static void state(Supplier<Boolean> element, String message) {
        notNull(element, "Supplier<Boolean> cannot be null");
        if (!element.get()) {
            sneakyThrows(new MojoExecutionException(message));
        }
    }

    public static void fail(String message) {
        sneakyThrows(new MojoExecutionException(message));
    }
}
