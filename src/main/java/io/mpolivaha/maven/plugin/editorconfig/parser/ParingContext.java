package io.mpolivaha.maven.plugin.editorconfig.parser;

import org.jspecify.annotations.Nullable;

import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.GlobExpression;

/**
 * This class represents the context of the parser. It is supposed to hold all the relevant
 * information required for the parser to do its job.
 * <p>
 * P.S: This class is intentionally not a java {@link Record record}, because it is by its nature mutable,
 * due to the fact that the components of the context do change once parser moves on. The COW is not that
 * great since each new line inside the .editorconfig file will cause context recreation.
 *
 * @author Mikhail Polivakha
 */
public class ParingContext {

  /**
   * Current line that parser is pointing at
   */
  private String line;

  /**
   * The number of the line inside the file
   */
  private int lineNumber;

  /**
   * The current section builder that we're assembling
   */
  private @Nullable SectionBuilder sectionBuilder;

  public ParingContext(String line) {
    this.line = line;
    this.lineNumber = 0;
    this.sectionBuilder = null;
  }

  public ParingContext newline(String line) {
    this.lineNumber++;
    this.line = line;
    return this;
  }

  // TODO tests
  public void startNewSection(GlobExpression expression) {
      Editorconfig editorconfig;
      if (sectionBuilder != null) {
          sectionBuilder.completeSection();
          editorconfig = sectionBuilder.getEditorconfig();
      } else {
          editorconfig = new Editorconfig();
      }
      sectionBuilder = editorconfig.new SectionBuilder(expression);
  }

  public String getLine() {
    return line;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public SectionBuilder getSectionBuilder() {
    return sectionBuilder;
  }
}
