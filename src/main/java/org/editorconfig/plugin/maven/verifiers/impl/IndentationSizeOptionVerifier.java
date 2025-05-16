/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.editorconfig.plugin.maven.verifiers.context.ContextKeys;
import org.jspecify.annotations.NonNull;

/**
 * {@link SpecOptionVerifier} for indentation_size option
 *
 * @author Mikhail Polivakha
 */
public class IndentationSizeOptionVerifier extends SpecOptionVerifier<Integer> {

    private Section section;
    private Charset charset;

    private static final String PREVIOUS_INDENT_LINE = "PREVIOUS_LINE_KEY";

    public IndentationSizeOptionVerifier() {
        super(Option.IDENT_SIZE);
    }

    @Override
    protected void onInit(
            Section section, VerifiersExecutionContext executionContext, File source) {
        Assert.notNull(section, "The section cannot be null at this point");
        this.section = section;
        this.charset = detectCharsetForGivenFile(section, executionContext);
    }

    private static Charset detectCharsetForGivenFile(
            Section section, VerifiersExecutionContext executionContext) {
        OptionValue<Charset> charset = section.getCharset();

        if (!charset.isUnset() && charset.isRecognized() && charset.getValue() != null) {
            return charset
                    .getValue(); // if possible, we rely on the charset the user has specified.
        }

        // otherwise, took the first of the detected. It must be either
        // UTF-8 or ASCII, which in 99% of cases would not matter for
        // further processing

        List<Charset> possibleCharsets = executionContext.get(ContextKeys.POSSIBLE_CHARSETS);

        if (possibleCharsets != null && !possibleCharsets.isEmpty()) {
            return possibleCharsets.get(0);
        }

        // Almost impossible to end up here, but if we are, then the
        // detector was not sure if we're using ascii or utf-8. We're
        // assuming a utf-16be here
        PluginConfiguration.getInstance()
                .<Log>getLog()
                .warn(
                        "Attention! Cannot verify the indentation size for the given file. Falling back to utf-16be"); // TODO: add file name
        return Charset.UTF_16BE;
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            Integer optionValue,
            OptionValidationResult result,
            @NonNull VerifiersExecutionContext executionContext) {
        int currentLineIndent;

        if (!line.isEmpty()) {
            Integer tabWidth = section.getTabWidthAsDigit();

            Assert.notNull(
                    tabWidth,
                    () ->
                            "The tab width should be guaranteed to be present, either in the form of 'tab_width' or 'indent_size' setting");

            currentLineIndent = line.getIndent(tabWidth, charset.getJavaCharset());

            Integer previousIndentLine = executionContext.get(PREVIOUS_INDENT_LINE);

            if (previousIndentLine != null && previousIndentLine != 0) {
                int thisIndent = currentLineIndent - previousIndentLine;
                if (thisIndent != optionValue && thisIndent > 0) {
                    result.addErrorMessage(
                            "The line number %d has the indentation level of %d columns in relation to previous line"
                                    .formatted(lineNumber, thisIndent));
                }
            }

        } else {
            currentLineIndent = 0;
        }

        executionContext.putLocal(PREVIOUS_INDENT_LINE, currentLineIndent);
    }

    @Override
    public OptionValue<Integer> getValueFromSection(Section section) {
        return section.getIndentationSize();
    }
}
