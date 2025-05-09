/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.config;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.editorconfig.plugin.maven.file.FileWalker;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.parser.EditorconfigParser;
import org.jspecify.annotations.Nullable;

/**
 * An object that is capable to build a Tree of EditorConfig files.
 *
 * @author Mikhail Polivakha
 */
public class TreeBuilder {

    /**
     * @implSpec <a href="https://spec.editorconfig.org/#file-processing">EditorConfig Specification Naming Section</a>
     */
    public static final String EDITORCONFIG_FILE_NAME = ".editorconfig";

    public static final TreeBuilder INSTANCE = new TreeBuilder();

    /**
     * Builds the tree of the .editorconfig files.
     *
     * @param start the starting location from which to start iteration
     * @return the root node of the .editorconfig files hierarchy
     */
    public @Nullable TreeNode buildTree(Path start) {

        AtomicReference<TreeNode> root = new AtomicReference<>();

        new FileWalker().walkRecursiveBFS(start, path -> {
            if (Objects.equals(path.toFile().getName(), EDITORCONFIG_FILE_NAME)) {
                Editorconfig editorconfig = EditorconfigParser.INSTANCE.parse(path);

                if (root.get() == null) {
                    root.set(new TreeNode(editorconfig));
                } else {
                    root.get().insertNode(editorconfig);
                }
            }
        });

        return root.get();
    }
}
