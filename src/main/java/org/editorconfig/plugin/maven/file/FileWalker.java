/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.file;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.utils.ExceptionUtils;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;

public class FileWalker {

    private static final BiFunction<String, Exception, String> DIRECTORY_VISIT_ERROR =
            (path, error) ->
                    "Error during walking the file-tree. Unable to fully traverse directory '%s'. Exception: \n%s"
                            .formatted(path, ExceptionUtils.getStackTrace(error));

    private static final BiFunction<String, Exception, String> FILE_INSPECTION_ERROR =
            (path, error) -> "Error during inspecting the file : '%s'. Exception: \n%s"
                    .formatted(path, ExceptionUtils.getStackTrace(error));

    public void walkRecursiveFilesInDirectory(Path root, Consumer<Path> contentConsumer)
            throws IOException {
        Files.walkFileTree(
                root, Set.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new FileVisitor<>() {

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                        return null;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                            throws IOException {
                        try {
                            contentConsumer.accept(file);
                        } catch (Throwable t) {
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc)
                            throws IOException {
                        if (exc != null) {
                            if (file.toFile().isDirectory()) {
                                ExecutionUtils.handleError(DIRECTORY_VISIT_ERROR.apply(
                                        file.toFile().getAbsolutePath(), exc));
                            } else {
                                ExecutionUtils.handleError(FILE_INSPECTION_ERROR.apply(
                                        file.toFile().getAbsolutePath(), exc));
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                            throws IOException {
                        if (exc != null) {
                            // This will fail the execution in case we're in strict mode
                            ExecutionUtils.handleError(DIRECTORY_VISIT_ERROR.apply(
                                    dir.toFile().getAbsolutePath(), exc));
                        } else {
                            PluginConfiguration.getInstance()
                                    .<Log>getLog()
                                    .debug(
                                            "The directory '%s' and all recursive files have been investigated successfully");
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
    }
}
