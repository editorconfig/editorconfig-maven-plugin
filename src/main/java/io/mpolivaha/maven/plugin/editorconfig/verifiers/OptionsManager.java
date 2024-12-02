package io.mpolivaha.maven.plugin.editorconfig.verifiers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.CachingInputStream;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.impl.CharsetOptionVerifier;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.impl.EndOfLineOptionVerifier;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.impl.IndentationSizeOptionVerifier;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.impl.InsertFinalNewLineOptionVerifier;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.impl.TrimTrailingWhitespaceOptionVerifier;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class OptionsManager {

  private static final OptionsManager INSTANCE = new OptionsManager(
      List.of(
          new CharsetOptionVerifier(Option.CHARSET),
          new EndOfLineOptionVerifier(Option.END_OF_LINE),
          new IndentationSizeOptionVerifier(Option.IDENT_SIZE),
          new InsertFinalNewLineOptionVerifier(Option.INSERT_FINAL_NEW_LINE),
          new TrimTrailingWhitespaceOptionVerifier(Option.TRIM_TRAILING_WHITESPACE)
      )
  );

  private final List<SpecOptionVerifier<?>> specOptionVerifiers;

  public OptionsManager(List<SpecOptionVerifier<?>> specOptionVerifiers) {
    specOptionVerifiers.sort(Comparator.comparing(SpecOptionVerifier::getOrder));
    this.specOptionVerifiers = specOptionVerifiers;
  }

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
    CachingInputStream cachingInputStream = new CachingInputStream(file.toFile());

    var context = new HashMap<String, Object>();

    for (SpecOptionVerifier specOptionVerifier : specOptionVerifiers) {
      OptionValidationResult check = specOptionVerifier.check(cachingInputStream, section, context);
      compoundResult.add(check);
      cachingInputStream.reset();
    }

    return compoundResult;
  }
}
