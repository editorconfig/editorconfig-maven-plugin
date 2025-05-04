/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.Map;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.model.IndentationStyle;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;

/**
 * {@link SpecOptionVerifier} for the {@link IndentationStyle}
 *
 * @author Mikhail Polivakha
 */
public class IndentationStyleOptionVerifier extends SpecOptionVerifier<IndentationStyle> {

    private Section section;

    public IndentationStyleOptionVerifier() {
        super(Option.IDENT_STYLE);
    }

    @Override
    protected void onInit(Section section, Map<String, Object> executionContext) {
        this.section = section;
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            IndentationStyle optionValue,
            OptionValidationResult result,
            Map<String, Object> context) {
        byte[] contentWithEol = line.getContentWithEol();

        if (optionValue.equals(IndentationStyle.SPACE)) {
            enforceSpaceIndentationOnly(lineNumber, result, contentWithEol);
        } else {
            enforceTabIndentationWhenPossible(lineNumber, result, contentWithEol);
        }
    }

    private void enforceTabIndentationWhenPossible(
            int lineNumber, OptionValidationResult result, byte[] contentWithEol) {
        int numberOfSoftTabs = 0;
        Integer tabWidth = section.getTabWidth();

        for (byte currentSymbol : contentWithEol) {

            if (currentSymbol == ' ') {
                numberOfSoftTabs++;
                if (numberOfSoftTabs >= tabWidth) {
                    result.addErrorMessage(
                            ("For the line number : %d indentation contains more then or equal to %d "
                                            + "soft tabs (spaces). however, the tab_width is set "
                                            + "to %d, which means that at least %d soft tabs can be replaced by tabs")
                                    .formatted(lineNumber, numberOfSoftTabs, tabWidth, tabWidth));
                }
            } else if (currentSymbol != '\t') {
                // met significant byte, indentation ends here
                break;
            }
        }
    }

    private static void enforceSpaceIndentationOnly(
            int lineNumber, OptionValidationResult result, byte[] contentWithEol) {
        for (int i = 0; i < contentWithEol.length; i++) {

            if (contentWithEol[i] != '\t' && contentWithEol[i] != ' ') {
                break;
            }

            if (contentWithEol[i] == '\t') {
                result.addErrorMessage(
                        "For line number %d expected the indentation to consist only of spaces, but a tab was met at position %d"
                                .formatted(lineNumber, i));
                return;
            }
        }
    }

    @Override
    public IndentationStyle getValueFromSection(Section section) {
        return section.getIndentationStyle();
    }
}
