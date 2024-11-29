package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import org.apache.maven.plugin.logging.Log;
import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * {@link SpecOptionVerifier} for the {@link Charset}
 *
 * @author Mikhail Polivakha
 */
public class CharsetOptionVerifier extends SpecOptionVerifier<Charset> {

  private UniversalDetector universalDetector;

  private static final Integer MAX_AMOUNT_OF_ITERATIONS = 10;
  private static final String ITERATION_NUMBER_CONTEXT_KEY = "ITERATION_NUMBER_CONTEXT_KEY";

  protected CharsetOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  protected OptionValidationResult checkInternal(InputStream content, Section section, Map<String, Object> executionContext) {
    Assert.state(content::markSupported, "The InputStream must support the mark/reset API");

    OptionValidationResult result = new OptionValidationResult(targetOption, getValueFromSection(section));
    this.universalDetector = new UniversalDetector();

    int iterationNum = 1;
    executionContext.put(ITERATION_NUMBER_CONTEXT_KEY, iterationNum);

    while (iterationNum <= MAX_AMOUNT_OF_ITERATIONS && !universalDetector.isDone()) {
      result = super.checkInternal(content, section, executionContext);
      iterationNum++;
      ExecutionUtils.executeExceptionally(content::reset);
      executionContext.put(ITERATION_NUMBER_CONTEXT_KEY, iterationNum);
    }

    if (!universalDetector.isDone()) {
      ExecutionUtils.handleError(
          "Exhausted all of the attempts when trying to determine the charset of the given file : %s. Therefore, the charset of the file cannot be accurately determined"
              .formatted(null) //TODO : add filename here
      );
    }
    return result;
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, Charset optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    if (!universalDetector.isDone()) {
      universalDetector.handleData(line.getContentWithEol());
    }
  }

  @Override
  protected void onCompletion(OptionValidationResult result, Charset targetOption) {
    if (universalDetector.isDone()) {
      var charset = java.nio.charset.Charset.forName(universalDetector.getDetectedCharset());
      if (!Objects.equals(charset, targetOption.getJavaCharset())) {
        result.addErrorMessage("Expected the file encoding : %s, but was : %s".formatted(targetOption.name(), charset.name()));
      }
    }
  }

  @Override
  public Charset getValueFromSection(Section section) {
    return section.getCharset();
  }
}
