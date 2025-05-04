/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.util.LinkedList;
import java.util.List;

import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.model.Option;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Result of validation of a given {@link Option} via {@link SpecOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
public class OptionValidationResult {

    private final @NonNull List<String> errorMessages;
    private final @NonNull Option option;
    private final @Nullable Object optionValue;

    public OptionValidationResult(Option option, Object optionValue) {
        this.option = option;
        this.optionValue = optionValue;
        this.errorMessages = new LinkedList<>();
    }

    public static OptionValidationResult skippedValidation(Option option) {
        return new OptionValidationResult(option, null);
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
        var finalErrorMessage = new StringBuilder("For option %s=%s found %d violation(-s):\n"
                .formatted(option.getKey(), optionValue, errorMessages.size()));
        errorMessages.forEach(s -> finalErrorMessage.append("\t- %s\n".formatted(s)));
        return finalErrorMessage.toString();
    }
}
