package io.mpolivaha.maven.plugin.editorconfig.verifiers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.CachingInputStream;
import java.io.IOException;
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
    for (SpecOptionVerifier specOptionVerifier : specOptionVerifiers) {
      OptionValidationResult check = specOptionVerifier.check(new CachingInputStream(file.toFile()), section);
      compoundResult.add(check);
    }
    return compoundResult;
  }
}
