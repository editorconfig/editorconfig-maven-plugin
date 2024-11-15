package io.mpolivaha.maven.plugin.editorconfig.verifiers;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper over the group of {@link OptionValidationResult option validation results}.
 * This instance presents all validation errors found on a specific file.
 *
 * @author Mikhail Polivakha
 */
public class CompoundOptionValidationResult {

  private final List<OptionValidationResult> validationResults;
  private final Path targetFile;

  public CompoundOptionValidationResult(Path targetFile) {
    this.targetFile = targetFile;
    validationResults = new LinkedList<>();
  }

  public void add(OptionValidationResult result) {
    this.validationResults.add(result);
  }

  @Override
  public String toString() {
    var toString = new StringBuilder(
        "In the target file : '%s' in total encountered %d violations: \n"
            .formatted(targetFile.toFile().getAbsolutePath(), validationResults.stream().mapToInt(OptionValidationResult::violationsCount).sum())
    );
    validationResults.forEach(result -> toString.append(result.renderErrorMessage()));
    return toString.toString();
  }
}
