package io.mpolivaha.maven.plugin.editorconfig.verifiers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.BufferedInputStream;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.common.Ordered;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This is base class for all other that aim to check for a specific {@link Option option}
 *
 * @param <T> the type, representing possible values of the {@link #targetOption} that it can take.
 *           For instance, in case of {@link Option#END_OF_LINE}, the {@code T} will be {@link EndOfLine}
 * @author Mikhail Polivakha
 */
public abstract class SpecOptionVerifier<T> implements Ordered {

  /**
   * The option that this {@link SpecOptionVerifier verifier} checks
   */
  protected final Option targetOption;

  public SpecOptionVerifier(Option targetOption) {
    this.targetOption = targetOption;
  }

  public OptionValidationResult check(InputStream content, Section section) {
    return checkInternal(content, section, new HashMap<>());
  }

  public OptionValidationResult check(InputStream content, Section section, Map<String, Object> executionContext) {
    return checkInternal(content, section, executionContext);
  }

  protected void onInit(Section section) {}

  /**
   * Checks, whether the content of the file is compliant with the current setting of the {@link #targetOption}
   *
   * @param content content of the file
   * @param section section that is assigned to the current file
   *
   * @return OptionViolations wrapped
   */
  protected OptionValidationResult checkInternal(InputStream content, Section section, Map<String, Object> executionContext) {
    T optionValue = getValueFromSection(section);

    try (var reader = new BufferedInputStream(content)) {
      onInit(section);
      int lineNumber = 1;
      ByteArrayLine line;
      OptionValidationResult result = new OptionValidationResult(targetOption, optionValue);

      do {
        line = reader.readLine();
        forEachLine(line, lineNumber, optionValue, result, executionContext);
        lineNumber++;
      } while (!line.isTheLastLine());

      onCompletion(result, optionValue);
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void forEachLine(ByteArrayLine line, int lineNumber, T optionValue, OptionValidationResult result, Map<String, Object> context);

  protected void onCompletion(OptionValidationResult result, T optionValue) {}

  /**
   * Function that extracts the value of the required type from given {@link Section}
   */
  public abstract T getValueFromSection(Section section);

  @Override
  public int getOrder() {
    return NORMAL;
  }
}
