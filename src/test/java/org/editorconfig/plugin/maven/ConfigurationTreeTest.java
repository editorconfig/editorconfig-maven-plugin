/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven;

import java.nio.file.Paths;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.editorconfig.plugin.maven.config.ConfigurationTree;
import org.editorconfig.plugin.maven.config.TreeNode;
import org.editorconfig.plugin.maven.model.Section;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled // TODO: implement the test
class ConfigurationTreeTest {

    private ConfigurationTree subject;

    @Test
    void testConfigurationTree_noAppropriateSectionAnywhereInTheTree() {
        // given.
        ConfigurationTree.build(build());
        subject = ConfigurationTree.getInstance();

        // when.
        Optional<Section> mergedSection =
                subject.findMerged(Paths.get("/first/second/third/forth/fifth.js"));

        // then.
        Assertions.assertThat(mergedSection).isEmpty();
    }

    @Test
    void testConfigurationTree_configGotMerged() {
        // given.
        ConfigurationTree.build(build());
        subject = ConfigurationTree.getInstance();

        // when.
        Optional<Section> mergedSection =
                subject.findMerged(Paths.get("/first/second/third/forth/fifth.js"));

        // then.
        Assertions.assertThat(mergedSection)
                .isPresent()
                .hasValueSatisfying(
                        new Condition<>(
                                section -> true,
                                "Merged section does not contain expected values given precedence in the effect"));
    }

    private static TreeNode build() {
        throw new UnsupportedOperationException();
    }
}
