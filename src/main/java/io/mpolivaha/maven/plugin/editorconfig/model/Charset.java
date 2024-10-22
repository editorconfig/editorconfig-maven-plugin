package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum Charset {
  UTF_8("utf-8", "utf-8-bom"),
  UTF_16("utf-16be", "utf-16le"),
  LATIN("latin1");

  private final Set<String> aliases;

  Charset(String... aliases) {
    this.aliases = Arrays.stream(aliases).collect(Collectors.toSet());
  }

  public static Optional<Charset> from(String charset) {
    for (Charset value : values()) {
      if (value.aliases.contains(charset.toLowerCase())) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }
}
