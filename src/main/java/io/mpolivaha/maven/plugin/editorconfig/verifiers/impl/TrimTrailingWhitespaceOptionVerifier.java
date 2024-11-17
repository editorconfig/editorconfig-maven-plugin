package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.util.Map;

/**
 * {@link SpecOptionVerifier} for trim_trailing_whitespace
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class TrimTrailingWhitespaceOptionVerifier extends SpecOptionVerifier<TrueFalse> {

  protected TrimTrailingWhitespaceOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, TrueFalse optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    int lastSignificantByteIndex = line.getEolStartsIndex() - 1;
    if (lastSignificantByteIndex >= 0) {
      byte second = line.at(lastSignificantByteIndex--);
      byte first = line.at(lastSignificantByteIndex);

      char symbol = (char) ((first << 8) + second);

      if (!Character.isWhitespace(symbol)) {
        result.addErrorMessage("For the line number %d expected no trailing characters, but found : %s".formatted(lineNumber, symbol));
      }
    }
  }

  @Override
  public TrueFalse getValueFromSection(Section section) {
    return section.getTrimTrailingWhitespace();
  }
}
