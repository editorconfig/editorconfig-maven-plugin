package io.mpolivaha.maven.plugin.editorconfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.jspecify.annotations.Nullable;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Section;

/**
 * Represents a tree data structure where nodes hold objects of {@link Editorconfig} type.
 * <p>
 * Such a structure is required in order to satisfy the following specification requirement //TODO add link
 *
 * @author Mikhail Polivakha
 */
public class ConfigurationTree {

    private final TreeNode root;

    public ConfigurationTree(TreeNode root) {
        Assert.notNull(root, "The root must not be empty");
        Assert.notNull(root.value, "The editorconfig struct must not be empty");
        Assert.state(root.value::isRoot, "The passed TreeNode must represent the root Editorconfig file");

        this.root = root;
    }

    /**
     * Assembles the merged {@link Section} resulting from traversing the {@link Editorconfig} file tree.
     *
     * @param file source code file to assemble merged {@link Section} for.
     * @return merged {@link Section}.
     */
    public Optional<Section> findMerged(Path file) {
        Queue<TreeNode> buffer = new LinkedList<>();
        buffer.add(root);

        return bfs(buffer, file, null);
    }

    /**
     * We have to employee BFS tre traversal algorithm here instead of DFS. Theoretically, DFS
     * would also work here, but with BFS the layers traversal is just more clear.
     * <p>
     * The core assumption of this method is that the latter {@link TreeNode elements of the queue} will have
     * precedence when containing appropriate section for given source code file. Therefore, just by iterating
     * over the queue and {@link Queue#poll() polling} elements from it, we can satisfy the order of precedence
     * defined in specification
     */
    private Optional<Section> bfs(Queue<TreeNode> buffer, Path file, @Nullable Section mergingAccumulator) {
        if (buffer.isEmpty()) {
            return Optional.ofNullable(mergingAccumulator);
        }

        TreeNode treeNode = buffer.poll();
        Editorconfig editorconfig = treeNode.value;

        Optional<Section> merged = editorconfig.findTargetSection(file).map(prioritizedSection -> {
            if (mergingAccumulator == null) {
                return prioritizedSection;
            } else {
                return mergingAccumulator.mergeWith(prioritizedSection);
            }
        });

        buffer.addAll(treeNode.children);

        return bfs(buffer, file, merged.orElse(null));
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
