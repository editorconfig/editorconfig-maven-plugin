package io.mpolivaha.maven.plugin.editorconfig.common;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Utility class for handling common execution use cases
 *
 * @author Mikhail Polivakha
 */
public class ExecutionUtils {

  public static void handleError(String errorMessage) {
    if (PluginConfiguration.getInstance().isStrictMode()) {
      Assert.sneakyThrows(new MojoExecutionException(errorMessage));
    } else {
      PluginConfiguration.getInstance().<Log>getLog().warn(errorMessage);
    }
  }
}
