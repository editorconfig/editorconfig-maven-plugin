/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.glob;

/**
 * {@link RuntimeException} that is thrown by {@link GlobExpressionParser} in case the glob expression is not valid.
 *
 * @author Mikhail Polivakha
 */
public class IncorrectGlobExpressionException extends RuntimeException {

    public IncorrectGlobExpressionException(String expression, String reason) {
        super("The glob expression : %s is not correct. Reason : %s".formatted(expression, reason));
    }
}
