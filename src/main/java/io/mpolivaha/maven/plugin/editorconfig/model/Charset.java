package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum Charset {
  // TODO: complete possible charsets and add possible aliases
  UTF_8(),
  LATIN(),
  ASCII();

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
