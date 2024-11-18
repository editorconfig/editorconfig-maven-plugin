package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.util.Map;
import java.util.Objects;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * {@link SpecOptionVerifier} for the {@link Charset}
 *
 * @author Mikhail Polivakha
 */
public class CharsetOptionVerifier extends SpecOptionVerifier<Charset> {

  private UniversalDetector universalDetector;

  protected CharsetOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  protected void onInit(Section section) {
    universalDetector = new UniversalDetector();
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
    } else {
      throw new IllegalStateException("There is not enough data to determine the charset of the input file");
    }
  }

  @Override
  public Charset getValueFromSection(Section section) {
    return section.getCharset();
  }
}
