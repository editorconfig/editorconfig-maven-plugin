package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import java.io.InputStream;

/**
 * This is base class for all other that aim to check for a specific {@link Option option}
 *
 * @param <T> the type, representing possible values of the {@link #targetOption} that it can take.
 *           For instance, in case of {@link Option#END_OF_LINE}, the {@code T} will be {@link EndOfLine}
 * @author Mikhail Polivakha
 */
public abstract class SpecOptionVerifier<T> {

  /**
   * The option that this {@link SpecOptionVerifier verifier} checks
   */
  protected final Option targetOption;

  protected SpecOptionVerifier(Option targetOption) {
    this.targetOption = targetOption;
  }

  public OptionValidationResult check(InputStream content, Section section) {
    return checkInternal(content, getValueFromSection(section));
  }

  /**
   * Checks, whether the content of the file is compliant with the current setting of the {@link #targetOption}
   *
   * @param content content of the file
   * @param optionValue value of option to check against
   * @return OptionViolations wrapped
   */
  abstract OptionValidationResult checkInternal(InputStream content, T optionValue);

  /**
   * Function that extracts the value of the required type from given {@link Section}
   */
  public abstract T getValueFromSection(Section section);
}
