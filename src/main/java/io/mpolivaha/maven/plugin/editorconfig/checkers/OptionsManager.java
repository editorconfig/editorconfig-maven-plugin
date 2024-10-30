package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import java.io.InputStream;

public class OptionsManager {

  private static OptionsManager INSTANCE = new OptionsManager();

  public static OptionsManager getInstance() {
    return INSTANCE;
  }

  public void accept(InputStream inputStream, Section section) {

  }
}
