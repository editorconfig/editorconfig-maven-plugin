/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility class for dealing with {@link Exception}
 *
 * @author Mikhail Polivakha
 */
public class ExceptionUtils {

    /**
     * Retrieving the stacktrace of the exception as a String
     *
     * @param t exception to fetch the stacktrace for
     * @return exception as {@link String}
     */
    public static String getStackTrace(Throwable t) {
        try (StringWriter writer = new StringWriter();
                PrintWriter wrapper = new PrintWriter(writer)) {

            t.printStackTrace(wrapper);

            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
