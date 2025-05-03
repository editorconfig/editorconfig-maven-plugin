/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.Map;
import java.util.Objects;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.TripleFunction;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;

/**
 * {@link SpecOptionVerifier} for {@link EndOfLine} option
 *
 * @author Mikhail Polivakha
 */
public class EndOfLineOptionVerifier extends SpecOptionVerifier<EndOfLine> {

    private static final TripleFunction<Integer, String, String, String> ERROR_MESSAGE_PRODUCER =
            (lienNum, expected, actual) ->
                    "In line %d expected %s, but was %s".formatted(lienNum, expected, actual);

    public EndOfLineOptionVerifier() {
        super(Option.END_OF_LINE);
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            EndOfLine optionValue,
            OptionValidationResult result,
            Map<String, Object> executionContext) {
        if (!line.isLastEmptyEOFLine()) {
            if (!Objects.equals(optionValue, line.getEndOfLine())) {
                // here, we're potentially checking the last line which might end with EOF
                // In this case, we'll 100% will fail the build, and I think it is the
                // right way to do this since the last line of the file is still a POSIX line
                result.addErrorMessage(ERROR_MESSAGE_PRODUCER.apply(
                        lineNumber,
                        optionValue.getPrintableSpecMarker(),
                        line.getEndOfLine().getPrintableSpecMarker()));
            }
        }
    }

    @Override
    public EndOfLine getValueFromSection(Section section) {
        return section.getEndOfLine();
    }
}
