package io.mpolivaha.maven.plugin.editorconfig.assertions;

import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Assertion class
 *
 * @author Mikhail Polivakha
 */
public class Assert {

  public static <T extends Throwable> void sneakyThrows(Throwable exception) throws T {
    throw (T) exception;
  }

  public static <T> void notNull(T element, String message) {
    if (element == null) {
      if (PluginConfiguration.getInstance().isStrictMode()) {
        sneakyThrows(new MojoExecutionException(message));
      } else {
        PluginConfiguration.getInstance().<Log>getLog().warn(message);
      }
    }
  }
}
