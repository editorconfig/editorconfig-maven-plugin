/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.io.File;
import java.io.IOException;

import org.editorconfig.plugin.maven.common.BufferedInputStream;
import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.common.Ordered;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.model.Section;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * This is base class for all other that aim to check for a specific {@link Option option}
 *
 * @param <T> the type, representing possible values of the {@link #targetOption} that it can take.
 *           For instance, in case of {@link Option#END_OF_LINE}, the {@code T} will be {@link EndOfLine}
 * @author Mikhail Polivakha
 */
public abstract class SpecOptionVerifier<T> implements Ordered {

    /**
     * The option that this {@link SpecOptionVerifier verifier} checks
     */
    protected final Option targetOption;

    public SpecOptionVerifier(Option targetOption) {
        this.targetOption = targetOption;
    }

    public OptionValidationResult check(
            CachingInputStream content, Section section, VerifiersExecutionContext context) {
        return checkInternal(content, section, context);
    }

    protected void onInit(
            Section section, VerifiersExecutionContext executionContext, File source) {}

    /**
     * Checks, whether the content of the file is compliant with the current setting of the {@link #targetOption}
     *
     * @param content content of the file
     * @param section section that is assigned to the current file. The section is expected to be already merged with
     *               all others that present in .editorconfig files in the tree.
     *
     * @return OptionViolations wrapped
     */
    protected OptionValidationResult checkInternal(
            CachingInputStream content,
            Section section,
            VerifiersExecutionContext executionContext) {

        OptionValue<T> optionValue = getValueFromSection(section);

        if (optionValue == null || optionValue.isUnset() || optionValue.getValue() == null) {
            return OptionValidationResult.skippedValidation(targetOption);
        }

        try (var reader = new BufferedInputStream(content)) {
            onInit(section, executionContext, content.getOriginalFile());
            int lineNumber = 1;
            ByteArrayLine line;
            OptionValidationResult result =
                    new OptionValidationResult(targetOption, optionValue.getValue());

            do {
                line = reader.readLine();
                forEachLine(line, lineNumber, optionValue.getValue(), result, executionContext);
                lineNumber++;
            } while (!line.isTheLastLine());

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void forEachLine(
            @NonNull ByteArrayLine line,
            int lineNumber,
            @NonNull T optionValue,
            @NonNull OptionValidationResult result,
            @NonNull VerifiersExecutionContext context);

    /**
     * Function that extracts the value of the required type from given {@link Section}
     */
    public abstract @Nullable OptionValue<T> getValueFromSection(Section section);

    @Override
    public int getOrder() {
        return NORMAL;
    }
}
