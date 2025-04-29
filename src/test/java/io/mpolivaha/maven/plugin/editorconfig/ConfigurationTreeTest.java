package io.mpolivaha.maven.plugin.editorconfig;

import static io.mpolivaha.maven.plugin.editorconfig.ConfigurationTree.*;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.GlobExpression;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.parser.EditorconfigParser;

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
                section -> true,
              "Merged section does not contain expected values given precedence in the effect"
            )
          );
    }

    private static TreeNode build() {
//        new EditorconfigParser().parse(Paths.get("configuration/.editorconfig"));

        try {
            TreeNode treeNode = new TreeNode(new EditorconfigParser().parse(Paths.get(ClassLoader.getSystemClassLoader()
              .getResource("configuration/.editorconfig")
              .toURI())));
            return treeNode;
        } catch (Exception e) {
            return null;
        }
//          .addChild(
//            new TreeNode()
//          )
//          .addChild(new TreeNode(new Editorconfig())); // that branch is off
    }

    public static void main(String[] args) {
        build();
    }
}
