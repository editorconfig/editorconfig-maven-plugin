package io.mpolivaha.maven.plugin.editorconfig.parser;

import java.io.InputStream;

public class EditorconfigParser {

  private String editorconfigLocation;

  public EditorconfigParser(String editorconfigLocation) {
    InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(editorconfigLocation);
    this.editorconfigLocation = editorconfigLocation;
  }
}
