package io.mpolivaha.maven.plugin.editorconfig.utils;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Utility class for handling common execution use cases
 *
 * @author Mikhail Polivakha
 */
public class ExecutionUtils {

  /**
   * Handles error depending on the way the {@link Param#STRICT_MODE} is configured
   *
   * @param errorMessage the error message to handle
   */
  public static void handleError(String errorMessage) {
    if (PluginConfiguration.getInstance().isStrictMode()) {
      Assert.sneakyThrows(new MojoExecutionException(errorMessage));
    } else {
      PluginConfiguration.getInstance().<Log>getLog().warn(errorMessage);
    }
  }
}
