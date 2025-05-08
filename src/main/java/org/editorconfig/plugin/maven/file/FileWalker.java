/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class exists mainly because the {@link java.nio.file.Files#walkFileTree(Path, Set, int, FileVisitor)} and similar APIs
 * traverse the directory using DFS. This is
 */
public class FileWalker {

    public void walkRecursiveBFS(Path root, Consumer<Path> contentConsumer)
            throws IOException {
        var queue = new LinkedList<Path>();
        queue.add(root);
        this.walkRecursiveBFS(queue, contentConsumer);
    }

    /**
     * Walks the files in the given directory using bread-first search.
     * Consider the following file structure:
     * <p>
     * <pre>
     * ├── file1
     * ├── file2
     * ├── file3
     * ├── l1
     * │    ├── l1_file1
     * │    ├── l1_file2
     * │    └── l2
     * │        ├── l2_file1
     * │        └── l3
     * └── v2
     *     └── v2_file1
     * </pre>
     * In the files would be processed in the following order:
     * <ol>
     *     <li>Files in the root dir (file1, file2, file3)</li>
     *     <li>Files in the inner dirs, either l1 or v2</li>
     *     <li>Files in the inner dirs, either l1 or v2</li>
     *     <li>Files in the inner dirs, either l2</li>
     * </ol>
     */
    private void walkRecursiveBFS(Queue<Path> paths, Consumer<Path> contentConsumer) {
        Path currentFile = paths.poll();

        if (Files.isDirectory(currentFile)) {
            File[] files = currentFile.toFile().listFiles();

            Queue<Path> subQueue = new LinkedList<>();

            for (var child : files) {
                if (child.isFile()) {
                    contentConsumer.accept(child.toPath());
                } else if (child.isDirectory()) {
                    subQueue.add(child.toPath());
                }
            }

            paths.addAll(subQueue);
            this.walkRecursiveBFS(paths, contentConsumer);
        } else {
            contentConsumer.accept(currentFile);
        }
    }
}
