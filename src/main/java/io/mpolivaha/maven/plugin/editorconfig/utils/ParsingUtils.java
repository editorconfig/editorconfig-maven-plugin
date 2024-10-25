package io.mpolivaha.maven.plugin.editorconfig.utils;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import java.util.Optional;

/**
 * Utils for parsing editorconfig files
 *
 * @author Mikhail Polivakha
 */
public class ParsingUtils {

  public static boolean isComment(String line) {
    Assert.notNull(line, "Line cannot be null");

    return line.trim().startsWith("#");
  }

  public static boolean isSection(String line) {
    Assert.notNull(line, "Line cannot be null");

    String trimmed = line.trim();
    return trimmed.startsWith("[") && trimmed.endsWith("]");
  }

  public static Optional<KeyValue> parseKeyValue(String line) {
    Assert.notNull(line, "Line cannot be null");

    String[] parts = line.trim().split("=");

    if (parts.length != 2) {
      return Optional.empty();
    }

    return Optional.of(new KeyValue(parts[0].trim(), parts[1].trim()));
  }

  public record KeyValue(String key, String value) {}
}
