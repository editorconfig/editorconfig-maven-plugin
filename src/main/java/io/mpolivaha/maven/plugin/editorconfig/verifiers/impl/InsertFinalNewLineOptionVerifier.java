package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;

import java.util.Map;

/**
 * {@link SpecOptionVerifier} for the insert_final_new_line option
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class InsertFinalNewLineOptionVerifier extends SpecOptionVerifier<TrueFalse>{

  public InsertFinalNewLineOptionVerifier() {
    super(Option.INSERT_FINAL_NEW_LINE);
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, TrueFalse optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    if (line.isTheLastLine()) {
      // The very last line should always be the '\0' line, otherwise we're not terminating the last line with any end of line symbol
      if (!line.isLastEmptyEOFLine()) {
        result.addErrorMessage("Expected the end_of_line symbol to be present, but file terminates with EOF");
      }
    }
  }

  @Override
  public TrueFalse getValueFromSection(Section section) {
    return section.getInsertFinalNewLine();
  }
}
