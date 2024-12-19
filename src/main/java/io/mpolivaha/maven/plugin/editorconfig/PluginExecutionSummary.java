package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.verifiers.CompoundOptionValidationResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Printable plugin execution summary. Contains possible validation errors on all discovered files.
 *
 * @author Mikhail Polivakha
 */
public class PluginExecutionSummary {

  public static final String HEADER = "Editorconfig Maven Plugin execution summary:\n\n";

  private final Collection<CompoundOptionValidationResult> errors;

  public PluginExecutionSummary(Collection<CompoundOptionValidationResult> errors) {
    this.errors = errors;
  }

  public PluginExecutionSummary add(CompoundOptionValidationResult result) {
    this.errors.add(result);
    return this;
  }

  @Override
  public String toString() {
    return renderSummary();
  }

  public String renderSummary() {
    if (errors.isEmpty()) {
      return renderNoErrors();
    } else {
      return assembleErrors();
    }
  }

  private String renderNoErrors() {
    return HEADER.concat("No errors discovered during .editorconfig validation of current project. Enjoy your day!");
  }

  private String assembleErrors() {
    StringBuilder result = new StringBuilder(HEADER);

    result.append("In current project found %d files that violate some of the .editorconfig rules:\n".formatted(errors.size()));

    for (var error : errors) {
      result.append(error.toString());
    }
    return result.toString();
  }
}
