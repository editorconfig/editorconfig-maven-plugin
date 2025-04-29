package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Section;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.CompoundOptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionsManager;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import io.mpolivaha.maven.plugin.editorconfig.file.FileWalker;
import io.mpolivaha.maven.plugin.editorconfig.parser.EditorconfigParser;
import java.io.IOException;
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
      try {
        var rootEditorConfigIs = getEditorConfigInputStream();
        Editorconfig editorconfig = new EditorconfigParser().parse(rootEditorConfigIs);
        new FileWalker().walkRecursiveFilesInDirectory(
            editorconfig.getLocation(),
            (recursivelyFoundFile) -> {
              editorconfig
                  .findTargetSection(recursivelyFoundFile)
                  .ifPresent(section -> delegateToOptionsManager(recursivelyFoundFile, section));
            });

        String summary = new PluginExecutionSummary(generationErrors).renderSummary();

        if (!generationErrors.isEmpty()) {
          throw new MojoFailureException(summary);
        } else {
          PluginConfiguration.getInstance().<Log>getLog().info(summary);
        }
      } catch (IOException e) {
        Assert.sneakyThrows(e);
      }
    }
  }

    private void setUpConfiguration() {
        PluginConfiguration.buildInstance(
            Map.of(
                Param.STRICT_MODE, strictMode,
                Param.LOG, getLog()
            )
        );
    }

    private Path getEditorConfigInputStream() throws MojoExecutionException {
    return new EditorConfigFileResolver()
        .findRootEditorConfig(project, rootEditorConfigFileLocation)
        .orElseThrow(
            () -> new MojoExecutionException("The specified .editorconfig file was not found : '%s'".formatted(rootEditorConfigFileLocation)));
  }

  private void delegateToOptionsManager(Path file, Section section) {
    try {
      OptionsManager.getInstance().check(file, section).ifNotValid(generationErrors::add);
    } catch (Throwable e) {
      Assert.sneakyThrows(e);
    }
  }
}
