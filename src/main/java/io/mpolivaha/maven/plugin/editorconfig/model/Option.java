package io.mpolivaha.maven.plugin.editorconfig.model;

import java.util.Optional;

public enum Option {

  IDENT_STYLE("indent_style"),
  IDENT_SIZE("indent_size"),
  TAB_WIDTH("tab_width"),
  END_OF_LINE("end_of_line"),
  CHARSET("charset"),
  TRIM_TRAILING_WHITESPACE("trim_trailing_whitespace"),
  INSERT_FINAL_NEW_LINE("trim_trailing_whitespace"),
  ROOT("root");

  private final String key;

  Option(String key) {
    this.key = key;
  }

  public static Optional<Option> from(String key) {
    for (Option value : values()) {
      if (value.key.equalsIgnoreCase(key)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  public String getKey() {
    return key;
  }
}
