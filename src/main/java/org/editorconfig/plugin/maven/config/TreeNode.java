/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.config;

import java.util.ArrayList;
import java.util.List;

import org.editorconfig.plugin.maven.model.Editorconfig;
import org.jspecify.annotations.NonNull;

/**
 * Node of the EditorConfig files tree.
 *
 * @author Mikhail Polivakha
 */
public class TreeNode {

    private final Editorconfig value;

    private final List<@NonNull TreeNode> children;

    public TreeNode(Editorconfig value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public TreeNode addChild(TreeNode treeNode) {
        this.children.add(treeNode);
        return this;
    }

    public Editorconfig getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    /**
     * Insert a given node into the node tree, represented by {@code this} node.
     *
     * @param newNode the node to insert into a tree, represented by {@code this} node.
     * @throws NodeInsertionException in case the insertion cannot happen with {@code this} node as a parent of {@code newNode}.
     */
    public void insertNode(Editorconfig newNode) throws NodeInsertionException {
        String location = extractLocation(this.value);
        String forInsert = extractLocation(newNode);

        if (!forInsert.startsWith(location)) {
            throw new NodeInsertionException();
        }

        for (TreeNode child : this.children) {

            String childLocation = extractLocation(child.getValue());

            if (forInsert.startsWith(childLocation)) {
                child.insertNode(newNode);
                return;
            }
        }

        // no child matched the location, but current node is still a parent for passed newNode
        this.addChild(new TreeNode(newNode));
    }

    private String extractLocation(Editorconfig editorconfig) {
        String location =
                editorconfig.getLocation().normalize().toAbsolutePath().toString();

        if (location.endsWith(TreeBuilder.EDITORCONFIG_FILE_NAME)) {
            location = location.substring(
                    0, location.length() - TreeBuilder.EDITORCONFIG_FILE_NAME.length());
        }

        return location;
    }

    /**
     * Occurs when the insertion into a given tree cannot because of various reasons.
     *
     * @author Mikhail Polivakha
     */
    public static class NodeInsertionException extends RuntimeException {}
}
