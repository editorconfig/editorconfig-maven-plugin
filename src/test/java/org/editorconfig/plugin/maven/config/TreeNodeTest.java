/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.config;

import java.nio.file.Paths;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link TreeNode}.
 *
 * @author Mikhail Polivakha
 */
class TreeNodeTest {

    @Test
    void testSimpleChildInsertion() {
        // given.
        TreeNode root = buildTestNodeTree();

        // when.
        root.insertNode(new Editorconfig(Paths.get("/root/third/.editorconfig")));

        // then.
        assertThat(root.getChildren()).hasSize(3);
        assertThat(root.getChildren())
                .extracting(treeNode ->
                        treeNode.getValue().getLocation().toAbsolutePath().toString())
                .containsOnly(
                        "/root/first/.editorconfig",
                        "/root/second/.editorconfig",
                        "/root/third/.editorconfig");
    }

    @Test
    void testInsertionSingleLevelNesting() {
        // given.
        TreeNode root = buildTestNodeTree();

        // when.
        root.insertNode(new Editorconfig(Paths.get("/root/second/inner/.editorconfig")));

        // then.
        assertThat(root.getChildren()).hasSize(2);
        assertThat(root.getChildren())
                .filteredOn(treeNode -> treeNode.getValue()
                        .getLocation()
                        .toString()
                        .equals("/root/first/.editorconfig"))
                .hasSize(1)
                .element(0)
                .extracting(TreeNode::getChildren, InstanceOfAssertFactories.list(TreeNode.class))
                .isEmpty();

        assertThat(root.getChildren())
                .filteredOn(treeNode -> treeNode.getValue()
                        .getLocation()
                        .toString()
                        .equals("/root/second/.editorconfig"))
                .hasSize(1)
                .element(0)
                .extracting(TreeNode::getChildren, InstanceOfAssertFactories.list(TreeNode.class))
                .hasSize(1)
                .element(0)
                .extracting(treeNode -> treeNode.getValue().getLocation().toString())
                .isEqualTo("/root/second/inner/.editorconfig");
    }

    @Test
    void testInsertionIntoWrongNodeParent() {
        // given.
        TreeNode root = buildTestNodeTree();

        // when / then
        assertThatThrownBy(() -> root.insertNode(
                        new Editorconfig(Paths.get("/another-root/third/.editorconfig"))))
                .isInstanceOf(TreeNode.NodeInsertionException.class);
    }

    private static TreeNode buildTestNodeTree() {
        return new TreeNode(new Editorconfig(Paths.get("/root/.editorconfig")))
                .addChild(new TreeNode(new Editorconfig(Paths.get("/root/first/.editorconfig"))))
                .addChild(new TreeNode(new Editorconfig(Paths.get("/root/second/.editorconfig"))));
    }
}
