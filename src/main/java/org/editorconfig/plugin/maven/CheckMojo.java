/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.config.ConfigurationTree;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.config.PluginConfiguration.Param;
import org.editorconfig.plugin.maven.config.TreeBuilder;
import org.editorconfig.plugin.maven.config.TreeNode;
import org.editorconfig.plugin.maven.file.FileWalker;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.verifiers.CompoundOptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.OptionsManager;

@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckMojo extends AbstractMojo {

    @Parameter(name = "strictMode", defaultValue = "true", required = true)
    private boolean strictMode;

    /**
     * Root editorconfig file location relative to the Maven's {@code project.basedir}.
     */
    @Parameter(name = "rootEditorConfigFileLocation")
    private String rootEditorConfigFileLocation;

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    private final Set<CompoundOptionValidationResult> generationErrors = new HashSet<>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        setUpConfiguration();

        generationErrors.clear();

        if (rootEditorConfigFileLocation != null && !rootEditorConfigFileLocation.isEmpty()) {
            var rootEditorConfig = resolveEditorConfig();

            TreeNode editorConfigFilesTree = TreeBuilder.INSTANCE.buildTree(rootEditorConfig);
            ConfigurationTree.build(editorConfigFilesTree);

            new FileWalker()
                    .walkRecursiveBFS(
                            editorConfigFilesTree.getValue().getParentDir(),
                            (recursivelyFoundFile) -> {
                                ConfigurationTree.getInstance()
                                        .findMerged(recursivelyFoundFile)
                                        .ifPresent(section -> delegateToOptionsManager(
                                                recursivelyFoundFile, section));
                            });

            String summary = new PluginExecutionSummary(generationErrors).renderSummary();

            if (!generationErrors.isEmpty()) {
                throw new MojoFailureException(summary);
            } else {
                PluginConfiguration.getInstance().<Log>getLog().info(summary);
            }
        }
    }

    private void setUpConfiguration() {
        PluginConfiguration.buildInstance(
                Map.of(Param.STRICT_MODE, strictMode, Param.LOG, getLog()));
    }

    private Path resolveEditorConfig() throws MojoExecutionException {
        return new EditorConfigFileResolver()
                .findRootEditorConfig(project, rootEditorConfigFileLocation)
                .orElseThrow(() -> new MojoExecutionException(
                        "The specified .editorconfig file was not found : '%s'"
                                .formatted(rootEditorConfigFileLocation)));
    }

    private void delegateToOptionsManager(Path file, Section section) {
        try {
            OptionsManager.getInstance().check(file, section).ifNotValid(generationErrors::add);
        } catch (Throwable e) {
            Assert.sneakyThrows(e);
        }
    }
}
