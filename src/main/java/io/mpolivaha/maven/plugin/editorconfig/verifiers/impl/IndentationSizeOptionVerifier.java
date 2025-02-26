package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.model.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.common.IndentationBlock;
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

  /**
   * The indentation block that is applicable for the given line
   */
  private static final String INDENTATION_BLOCK = "INDENTATION_BLOCK";

  public IndentationSizeOptionVerifier() {
    super(Option.IDENT_SIZE);
  }

  @Override
  protected void onInit(Section section) {
    this.section = section;
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, Integer optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    Assert.notNull(section, "The section cannot be null at this point");
    var currentIndentationBlock = (IndentationBlock) executionContext.getOrDefault(INDENTATION_BLOCK, IndentationBlock.root());

    if (line.isEmpty()) {

    }

    executionContext.put(
        PREVIOUS_LINE_KEY,
        discoverIndentationBlock(line, optionValue, currentIndentationBlock)
    );
  }

  private static IndentationBlock discoverIndentationBlock(ByteArrayLine line, Integer optionValue, IndentationBlock indentationBlock) {
    if (line.startsNewCodeBlock())  {
      return indentationBlock.next(optionValue);
    } else {
      return indentationBlock;
    }
  }

  @Override
  public Integer getValueFromSection(Section section) {
    return section.getIndentationSize();
  }
}
