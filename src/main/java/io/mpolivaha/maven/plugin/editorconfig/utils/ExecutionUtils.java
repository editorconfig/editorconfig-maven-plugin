/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.utils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ThrowingRunnable;
import io.mpolivaha.maven.plugin.editorconfig.common.ThrowingSupplier;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;

/**
 * Utility class for handling common execution use cases
 *
 * @author Mikhail Polivakha
 */
public class ExecutionUtils {

    /**
     * Handles error depending on the way the {@link Param#STRICT_MODE} is configured
     *
     * @param errorMessage the error message to handle
     */
    public static void handleError(String errorMessage) {
        if (PluginConfiguration.getInstance().isStrictMode()) {
            Assert.sneakyThrows(new MojoExecutionException(errorMessage));
        } else {
            PluginConfiguration.getInstance().<Log>getLog().warn(errorMessage);
        }
    }

    public static <T, E extends Throwable> void executeExceptionally(
            ThrowingRunnable<T, E> throwingRunnable) {
        try {
            throwingRunnable.run();
        } catch (Throwable e) {
            Assert.sneakyThrows(e);
        }
    }

    public static <T, E extends Throwable> T mapExceptionally(
            ThrowingSupplier<T, E> throwingRunnable) {
        try {
            return throwingRunnable.get();
        } catch (Throwable e) {
            Assert.sneakyThrows(e);
            return null; // unreachable statement
        }
    }
}
