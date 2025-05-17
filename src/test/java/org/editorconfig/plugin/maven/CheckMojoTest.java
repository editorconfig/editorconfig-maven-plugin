/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven;

import static org.editorconfig.plugin.maven.utils.ExecutionUtils.executeExceptionally;
import static org.editorconfig.plugin.maven.utils.ExecutionUtils.mapExceptionally;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckMojoTest {

    @Test
    void testE2EScenario() {
        Assertions
          .assertThatThrownBy(
            () -> instantiateMojo(testFile("e2e/single-root-config/pom.xml"), ".editorconfig").execute())
          .isInstanceOf(MojoFailureException.class)
          .hasMessageContainingAll(
            "In current project found 3 files that violate some of the .editorconfig rules",
            "pom.xml' in total encountered 1 violations", // indentation
            "MyEntity.java' in total encountered 2 violations", // indentation
            "config.yaml' in total encountered 2 violations" // cr instead of lf
          );
    }

    private static File testFile(String testFile) throws URISyntaxException {
        return new File(ClassLoader.getSystemClassLoader().getResource(testFile).toURI());
    }

    private static CheckMojo instantiateMojo(File pomXml, String editorconfig) {
        CheckMojo checkMojo = new CheckMojo();
        Field project = getField("project");
        Field strictMode = getField("strictMode");
        Field rootEditorConfigFileLocation = getField("rootEditorConfigFileLocation");

        MavenProject mavenProject = new MavenProject();
        mavenProject.setFile(pomXml);
        executeExceptionally(() -> project.set(checkMojo, mavenProject));
        executeExceptionally(() -> strictMode.set(checkMojo, true));
        executeExceptionally(() -> rootEditorConfigFileLocation.set(checkMojo, editorconfig));
        return checkMojo;
    }

    private static Field getField(String name) {
        return mapExceptionally(() -> {
            Field declaredField = CheckMojo.class.getDeclaredField(name);
            declaredField.setAccessible(true);
            return declaredField;
        });
    }
}
