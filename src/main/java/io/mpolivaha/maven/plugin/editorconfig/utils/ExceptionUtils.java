package io.mpolivaha.maven.plugin.editorconfig.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility class for dealing with {@link Exception}
 *
 * @author Mikhail Polivakha
 */
public class ExceptionUtils {

  /**
   * Retrieving the stacktrace of the exception as a String
   *
   * @param t exception to fetch the stacktrace for
   * @return exception as {@link String}
   */
  public static String getStackTrace(Throwable t) {
    try (
        StringWriter writer = new StringWriter();
        PrintWriter wrapper = new PrintWriter(writer)) {

      t.printStackTrace(wrapper);

      return writer.getBuffer().toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
