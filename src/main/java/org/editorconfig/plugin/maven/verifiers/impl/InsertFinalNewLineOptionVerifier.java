/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.jspecify.annotations.NonNull;

/**
 * {@link SpecOptionVerifier} for the insert_final_new_line option
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class InsertFinalNewLineOptionVerifier extends SpecOptionVerifier<TrueFalse> {

    public InsertFinalNewLineOptionVerifier() {
        super(Option.INSERT_FINAL_NEW_LINE);
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            TrueFalse optionValue,
            OptionValidationResult result,
            @NonNull VerifiersExecutionContext executionContext) {
        if (line.isTheLastLine()) {
            // The very last line should always be the '\0' line, otherwise we're not terminating
            // the last line with any end of line symbol
            if (!line.isLastEmptyEOFLine()) {
                result.addErrorMessage(
                        "Expected the end_of_line symbol to be present, but file terminates with EOF");
            }
        }
    }

    @Override
    public TrueFalse getValueFromSection(Section section) {
        return section.getInsertFinalNewLine();
    }
}
