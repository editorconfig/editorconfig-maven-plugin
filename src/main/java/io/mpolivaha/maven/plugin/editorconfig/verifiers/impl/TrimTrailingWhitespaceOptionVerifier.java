package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;

public class TrimTrailingWhitespaceOptionVerifier extends SpecOptionVerifier<TrueFalse> {

  protected TrimTrailingWhitespaceOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, TrueFalse optionValue, OptionValidationResult result) {
    // TODO
  }

  @Override
  public TrueFalse getValueFromSection(Section section) {
    return section.getTrimTrailingWhitespace();
  }
}
