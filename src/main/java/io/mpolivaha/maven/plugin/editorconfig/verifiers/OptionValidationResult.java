package io.mpolivaha.maven.plugin.editorconfig.verifiers;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import java.util.LinkedList;
import java.util.List;

/**
 * Result of validation of a given {@link Option} via {@link SpecOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
public class OptionValidationResult {

  private final List<String> errorMessages;
  private final Option option;
  private final Object optionValue;

  public OptionValidationResult(Option option, Object optionValue) {
    this.option = option;
    this.optionValue = optionValue;
    this.errorMessages = new LinkedList<>();
  }

  public void addErrorMessage(String errorMessage) {
    errorMessages.add(errorMessage);
  }

  public int violationsCount() {
    return errorMessages.size();
  }

  public boolean noErrors() {
    return errorMessages.isEmpty();
  }

  public String renderErrorMessage() {
    if (noErrors()) {
      Assert.fail("Called renderErrorMessage() on non-erroneous OptionViolations");
      return null; // unreachable code
    }
    var finalErrorMessage = new StringBuilder("For option %s=%s found %d violation(-s):\n".formatted(
        option.getKey(),
        optionValue,
        errorMessages.size()
    ));
    errorMessages.forEach(s -> finalErrorMessage.append("\t- %s\n".formatted(s)));
    return finalErrorMessage.toString();
  }
}
