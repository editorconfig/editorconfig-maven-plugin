package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OptionsManager {

  private static OptionsManager INSTANCE = new OptionsManager();

  private List<SpecOptionVerifier<?>> specOptionVerifiers;

  public static OptionsManager getInstance() {
    return INSTANCE;
  }

  @SuppressWarnings("rawtypes")
  public void check(InputStream inputStream, Section section) {
    String content = getContent(inputStream);
    for (SpecOptionVerifier specOptionVerifier : specOptionVerifiers) {
      OptionValidationResult check = specOptionVerifier.check(content, section);
    }
  }

  private static String getContent(InputStream inputStream) {
    try {
      return new String(inputStream.readAllBytes());
    } catch (IOException e) {
      Assert.sneakyThrows(e);
      return null; // unreachable code
    }
  }
}
