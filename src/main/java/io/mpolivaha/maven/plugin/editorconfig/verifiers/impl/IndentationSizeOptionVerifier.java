package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.util.Map;

public class IndentationSizeOptionVerifier extends SpecOptionVerifier<Integer> {

  private Section section;

  private static final String PREVIOUS_LINE_KEY = "PREVIOUS_LINE_KEY";


  protected IndentationSizeOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  protected void onInit(Section section) {
    this.section = section;
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, Integer optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    // TODO: assert section is not null
    ByteArrayLine previous;
    if ((previous = (ByteArrayLine) executionContext.get(PREVIOUS_LINE_KEY)) == null) {
      executionContext.put(PREVIOUS_LINE_KEY, line);
      return;
    }

    int previousLength = previous.length();
    int currentLength = line.length();

    // TODO:
    result.addErrorMessage("The indentation level between lines %d and %d differs from the configured level of indentation, which is ");
  }

  @Override
  public Integer getValueFromSection(Section section) {
    return section.getIndentationSize();
  }
}
