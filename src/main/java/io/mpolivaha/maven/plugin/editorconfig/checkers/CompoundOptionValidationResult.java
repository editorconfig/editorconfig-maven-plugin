package io.mpolivaha.maven.plugin.editorconfig.checkers;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper over the group of {@link OptionValidationResult option validation results}.
 *
 * @author Mikhail Polivakha
 */
public class CompoundOptionValidationResult {

  private final List<OptionValidationResult> validationResults;

  public CompoundOptionValidationResult() {
    validationResults = new LinkedList<>();
  }

  public void add(OptionValidationResult result) {
    this.validationResults.add(result);
  }
}
