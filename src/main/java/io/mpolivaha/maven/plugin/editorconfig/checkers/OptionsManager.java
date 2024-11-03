package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OptionsManager {

  private static final OptionsManager INSTANCE = new OptionsManager();

  private List<SpecOptionVerifier<?>> specOptionVerifiers;

  public static OptionsManager getInstance() {
    return INSTANCE;
  }

  /**
   * Check for possible .editorconfig violations on a single file.
   *
   * @param file - file to check for violations
   * @param section - relevant {@link Section} of the appropriate .editorconfig file to be applied for the given file
   * @return CompoundOptionValidationResult that encapsulates all validation errors that happened
   */
  @SuppressWarnings("rawtypes")
  public CompoundOptionValidationResult check(Path file, Section section) throws IOException {
    var compoundResult = new CompoundOptionValidationResult(file);
    String content = getContent(Files.newInputStream(file));
    for (SpecOptionVerifier specOptionVerifier : specOptionVerifiers) {
      OptionValidationResult check = specOptionVerifier.check(content, section);
      compoundResult.add(check);
    }
    return compoundResult;
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
