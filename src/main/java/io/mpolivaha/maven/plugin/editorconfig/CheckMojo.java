package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import java.util.EnumMap;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckMojo extends AbstractMojo {

  @Parameter(name = "failOnError", defaultValue = "true", required = true)
  private boolean failOnError;

  @Parameter(name = "editorconfig")
  private String editorConfigLocation;

  public void execute() throws MojoExecutionException, MojoFailureException {
    PluginConfiguration.buildInstance(
        Map.of(
            Param.FAIL_ON_ERROR, failOnError,
            Param.LOG, getLog()
        )
    );

    if (editorConfigLocation != null && !editorConfigLocation.isEmpty()) {

    } else {

    }
  }
}
