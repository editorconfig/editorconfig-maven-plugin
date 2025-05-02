/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.model.Section;
import org.jspecify.annotations.Nullable;

import org.editorconfig.plugin.maven.model.Editorconfig;

/**
 * Represents a tree data structure where nodes hold objects of {@link Editorconfig} type.
 * <p>
 * Such a structure is required in order to satisfy the following specification requirement //TODO add link
 * Typically, we need only a single instance of the {@link ConfigurationTree}, since the tree of
 * .editorconfig file hierarchy is static during processing.
 *
 * @author Mikhail Polivakha
 */
public class ConfigurationTree {

    private final TreeNode root;

    private static volatile ConfigurationTree INSTANCE;

    private ConfigurationTree(TreeNode root) {
        Assert.notNull(root, "The root must not be empty");
        Assert.notNull(root.value, "The editorconfig struct must not be empty");
        Assert.state(
                root.value::isRoot,
                "The passed TreeNode must represent the root Editorconfig file");

        this.root = root;
    }

    public static ConfigurationTree getInstance() {
        return INSTANCE;
    }

    public static synchronized void build(TreeNode treeNode) {
        if (INSTANCE == null) {
            INSTANCE = new ConfigurationTree(treeNode);
        }
    }

    /**
     * Assembles the merged {@link Section} resulting from traversing the {@link Editorconfig} file tree.
     *
     * @param file source code file to assemble merged {@link Section} for.
     * @return merged {@link Section}. {@link Optional#empty()} if no appropriate {@link Section} was found for give file.
     */
    public Optional<Section> findMerged(Path file) {
        Queue<TreeNode> buffer = new LinkedList<>();
        buffer.add(root);

        return bfs(buffer, file, null);
    }

    /**
     * We have to employee BFS tree traversal algorithm here instead of DFS. Theoretically, DFS
     * would also work here, but with BFS the layers traversal is just more clear.
     * <p>
     * The core assumption of this method is that the latter {@link TreeNode elements of the queue} have higher
     * precedence when containing appropriate section for given source code file. Therefore, just by iterating
     * over the queue and {@link Queue#poll() polling} elements from it, we can satisfy the order of precedence
     * defined in specification. // TODO add a link to a specification section
     */
    private Optional<Section> bfs(
            Queue<TreeNode> buffer, Path file, @Nullable Section mergingAccumulator) {
        if (buffer.isEmpty()) {
            return Optional.ofNullable(mergingAccumulator);
        }

        TreeNode treeNode = buffer.poll();
        Editorconfig editorconfig = treeNode.value;

        Section merged = editorconfig
                .findTargetSection(file)
                .map(prioritizedSection -> {
                    buffer.addAll(
                            treeNode.children); // adding elements into a queue traversal only in
                    // case we found a matching section

                    if (mergingAccumulator == null) {
                        return prioritizedSection;
                    } else {
                        return mergingAccumulator.mergeWith(prioritizedSection);
                    }
                })
                .orElse(mergingAccumulator); // meaning, we have not found appropriate section in
        // the given .editorconfig file

        return bfs(buffer, file, merged);
    }

    static class TreeNode {
        private final Editorconfig value;

        private final List<TreeNode> children;

        public TreeNode(Editorconfig value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public TreeNode addChild(TreeNode treeNode) {
            this.children.add(treeNode);
            return this;
        }
    }
}
