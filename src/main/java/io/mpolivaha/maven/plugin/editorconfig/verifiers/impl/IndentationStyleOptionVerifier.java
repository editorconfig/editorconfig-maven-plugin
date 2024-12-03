package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.util.Map;

public class IndentationStyleOptionVerifier extends SpecOptionVerifier<IndentationStyle> {

  protected IndentationStyleOptionVerifier() {
    super(Option.IDENT_STYLE);
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, IndentationStyle optionValue, OptionValidationResult result,
      Map<String, Object> context) {

  }

  @Override
  public IndentationStyle getValueFromSection(Section section) {
    return null;
  }
}
