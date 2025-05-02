/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Utils for working with {@link Stream streams}
 *
 * @author Mikhail Polivakha
 */
public class StreamUtils {

    /**
     * Iterate over the elements of the {@link Stream} while having an index for each element being processed
     *
     * @param stream stream of elements
     * @param consumer {@link BiConsumer} that accepts th elements of the {@link Stream} and their indexes. Indexes are 1-based
     * @param <T> type of element of the {@link Stream}
     */
    public static <T> void forEachIndexed(Stream<T> stream, BiConsumer<T, Integer> consumer) {
        AtomicInteger index = new AtomicInteger(1);
        stream.forEach(t -> consumer.accept(t, index.getAndIncrement()));
    }
}
