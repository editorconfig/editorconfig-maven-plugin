package io.mpolivaha.maven.plugin.editorconfig.model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum Charset {
  UTF_8(StandardCharsets.UTF_8, "utf-8", "utf-8-bom"),
  UTF_16LE(StandardCharsets.UTF_16LE, "utf-16le"),
  UTF_16BE(StandardCharsets.UTF_16BE, "utf-16be"),
  LATIN1(StandardCharsets.ISO_8859_1, "latin1");

  private final Set<String> aliases;
  private final java.nio.charset.Charset javaCharset;

  Charset(java.nio.charset.Charset charset, String... aliases) {
    this.javaCharset = charset;
    this.aliases = Arrays.stream(aliases).collect(Collectors.toSet());
  }

  public java.nio.charset.Charset getJavaCharset() {
    return javaCharset;
  }

  public static Optional<Charset> from(java.nio.charset.Charset charset) {
    for (Charset value : values()) {
      if (value.javaCharset.equals(charset)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
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
