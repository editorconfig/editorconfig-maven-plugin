package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.util.Map;

/**
 * {@link SpecOptionVerifier} for indentation_size option
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
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
    Assert.notNull(section, "The section cannot be null at this point");

    ByteArrayLine previous;
    if ((previous = (ByteArrayLine) executionContext.get(PREVIOUS_LINE_KEY)) == null) {
      executionContext.put(PREVIOUS_LINE_KEY, line);
      return;
    }

    // TODO: Here, we reclaculate the indent of previous line, which can in fact be avoided
    int previousIndent = previous.getIndentInColumns(section.getTabWidth());
    int thisIndent = line.getIndentInColumns(section.getTabWidth());

    if (previousIndent != thisIndent) {
      int indentLevel = Math.abs(previousIndent - thisIndent);
      if (indentLevel != optionValue) {
        result.addErrorMessage(
            "The indentation level between lines %d and %d differs from the configured level of indentation. Required: %d, Actual : %d"
                .formatted(lineNumber - 1, lineNumber, optionValue, indentLevel)
        );
      }
    }

    executionContext.put(PREVIOUS_LINE_KEY, line);
  }

  @Override
  public Integer getValueFromSection(Section section) {
    return section.getIndentationSize();
  }
}
