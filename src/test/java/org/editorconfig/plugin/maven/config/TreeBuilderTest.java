/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.config;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link TreeBuilder}.
 *
 * @author Mikhail Polivakha
 */
class TreeBuilderTest {

    @Test
    void buildTree_ValidPath_ValidTreeNode() throws URISyntaxException {
        // given.
        File start = new File(ClassLoader.getSystemClassLoader()
                .getResource("tree-builder/first")
                .toURI());

        // when.
        TreeNode root = new TreeBuilder().buildTree(start.toPath());

        // then.
        assertThat(root).satisfies(treeNode -> {
            assertThat(treeNode.getValue().getLocation().toString())
                    .endsWith("/tree-builder/first/.editorconfig");
            assertThat(treeNode.getChildren()).hasSize(2);
            assertThat(treeNode.getChildren())
                    .extracting(it -> it.getValue().getLocation().toString())
                    .allSatisfy(s -> {
                        assertThat(s)
                                .containsAnyOf(
                                        "/tree-builder/first/level1/.editorconfig",
                                        "/tree-builder/first/level2/.editorconfig");
                    });

            TreeNode deepNode = treeNode.getChildren().stream()
                    .filter(it -> it.getValue()
                            .getLocation()
                            .toString()
                            .endsWith("/tree-builder/first/level1/.editorconfig"))
                    .findFirst()
                    .orElseThrow();

            assertThat(deepNode.getChildren()).hasSize(1);
        });
    }
}
