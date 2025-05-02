/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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

    public boolean isValid() {
        return validationResults.stream().allMatch(OptionValidationResult::noErrors);
    }

    public void ifNotValid(Consumer<CompoundOptionValidationResult> action) {
        if (!isValid()) {
            action.accept(this);
        }
    }

    @Override
    public String toString() {
        var toString = new StringBuilder("In the file : '%s' in total encountered %d violations: \n"
                .formatted(
                        targetFile.toFile().getAbsolutePath(),
                        validationResults.stream()
                                .mapToInt(OptionValidationResult::violationsCount)
                                .sum()));
        validationResults.forEach(result -> toString.append(result.renderErrorMessage()));
        return toString.toString();
    }
}
