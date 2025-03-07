package io.mpolivaha.maven.plugin.editorconfig;

import static io.mpolivaha.maven.plugin.editorconfig.ConfigurationTree.*;

import java.nio.file.Paths;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Section;

class ConfigurationTreeTest {

    private ConfigurationTree subject;

    @Test
    void testConfigurationTree_noAppropriateSectionAnywhereInTheTree() {
        // given.
        ConfigurationTree.build(build());
        subject = ConfigurationTree.getInstance();

        // when.
        Optional<Section> mergedSection = subject.findMerged(Paths.get("/first/second/third/forth/fifth.js"));

        // then.
        Assertions.assertThat(mergedSection).isEmpty();
    }

    @Test
    void testConfigurationTree_configGotMerged() {
        // given.
        ConfigurationTree.build(build());
        subject = ConfigurationTree.getInstance();

        // when.
        Optional<Section> mergedSection = subject.findMerged(Paths.get("/first/second/third/forth/fifth.js"));

        // then.
        Assertions.assertThat(mergedSection)
          .isPresent()
          .hasValueSatisfying(
            new Condition<>(
                section -> ,
              "Merged section does not contain expected values given precedence in the effect"
            )
          );
    }

    private TreeNode build() {
        return new TreeNode(new Editorconfig())
          .addChild(new TreeNode(new Editorconfig()))
          .addChild(new TreeNode(new Editorconfig())); // that branch is off
    }
}