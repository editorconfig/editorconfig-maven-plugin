/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.common;

/**
 * {@link java.util.function.Function Function-like} functional interface, that accepts 3 arguments
 *
 * @author Mikhail Polivakha
 */
@FunctionalInterface
public interface TripleFunction<I1, I2, I3, O> {

    O apply(I1 input1, I2 input2, I3 input3);
}
