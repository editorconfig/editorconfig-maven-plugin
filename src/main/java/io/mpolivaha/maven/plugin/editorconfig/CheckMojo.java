package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.checkers.OptionsManager;
import io.mpolivaha.maven.plugin.editorconfig.common.ThrowingSupplier;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import io.mpolivaha.maven.plugin.editorconfig.file.FileWalker;
import io.mpolivaha.maven.plugin.editorconfig.parser.EditorconfigParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckMojo extends AbstractMojo {

  @Parameter(name = "strictMode", defaultValue = "true", required = true)
  private boolean strictMode;

  @Parameter(name = "editorconfig")
  private String editorConfigLocation;

  public void execute() throws MojoExecutionException, MojoFailureException {
    PluginConfiguration.buildInstance(
        Map.of(
            Param.STRICT_MODE, strictMode,
            Param.LOG, getLog()
        )
    );

    if (editorConfigLocation != null && !editorConfigLocation.isEmpty()) {
      Editorconfig editorconfig = new EditorconfigParser().parse(editorConfigLocation);
      try {
        new FileWalker().walkRecursiveFilesInDirectory(
            editorconfig.getLocation(),
            (path, inputStreamProducer) -> {
              editorconfig.findTargetSection(path).ifPresent(section -> delegateToOptionsManager(inputStreamProducer, section));
            });
      } catch (IOException e) {
        Assert.sneakyThrows(e);
      }
    } else {
      // TODO: implement file tree search
    }
  }

  private static void delegateToOptionsManager(ThrowingSupplier<InputStream, Throwable> inputStreamProducer, Section section) {
    try {
      OptionsManager.getInstance().check(inputStreamProducer.get(), section);
    } catch (Throwable e) {
      Assert.sneakyThrows(e);
    }
  }
}
