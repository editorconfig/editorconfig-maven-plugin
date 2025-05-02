/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation serves mainly as a documentation marker for the API, that is considered to
 * be thread-safe.
 * <p>
 * If the {@link ElementType#TYPE type} is marked as {@link ThreadSafe}, then all the
 * <strong>public</strong> APIs of this type are safe to be used concurrently. Can be
 * placed on a single method to indicate that only the given method is thread safe,
 * and can be placed on the package level in the <code>package-info.java</code> to mark
 * all of the <strong>public</strong> APIs in this package as thread-safe.
 *
 * @author Mikhail Polivakha
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
public @interface ThreadSafe {}
