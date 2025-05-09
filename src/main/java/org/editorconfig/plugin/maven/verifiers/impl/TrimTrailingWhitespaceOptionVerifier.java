/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.PluginCharsetDetector;
import org.editorconfig.plugin.maven.config.PluginConfiguration;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;

import static org.editorconfig.plugin.maven.verifiers.context.ContextKeys.POSSIBLE_CHARSETS;

/**
 * {@link SpecOptionVerifier} for trim_trailing_whitespace
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class TrimTrailingWhitespaceOptionVerifier extends SpecOptionVerifier<TrueFalse> {

    public TrimTrailingWhitespaceOptionVerifier() {
        super(Option.TRIM_TRAILING_WHITESPACE);
    }

    @Override
    protected void onInit(Section section, Map<String, Object> executionContext, File source) {
        super.onInit(section, executionContext, source);
        Charset detectedCharset = getDetectedCharset(executionContext);

        if (detectedCharset == null) {
            warnNoCharsetDetected(source);
        }
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            TrueFalse optionValue,
            OptionValidationResult result,
            Map<String, Object> executionContext) {
        Charset charsetForParing = getDetectedCharset(executionContext);

        if (charsetForParing == null) {
            return;
        }

        InputStreamReader lineReader = new InputStreamReader(
                new ByteArrayInputStream(line.getContentWithEol()),
                charsetForParing.getJavaCharset());

        Character thisCharacter;
        Character prevCharacter = null;
        boolean haveTrailingWhiteSpace = false;

        while ((thisCharacter = ExecutionUtils.mapExceptionally(() -> (char) lineReader.read()))
                != -1) {

            if (isSingleCharEOL(line, thisCharacter)
                    || isMultiCharEol(line, thisCharacter, prevCharacter)) {
                if (haveTrailingWhiteSpace) {
                    result.addErrorMessage("For the line number %d expected no trailing characters"
                            .formatted(lineNumber));
                }
                break;
            }

            haveTrailingWhiteSpace = Character.isWhitespace(thisCharacter);
            prevCharacter = thisCharacter;
        }
    }

    private static boolean isSingleCharEOL(ByteArrayLine line, char character) {
        return line.getEndOfLine().isSingleCharacter()
                && line.getEndOfLine().getEolSymbol().charAt(0) == character;
    }

    private static boolean isMultiCharEol(ByteArrayLine line, char thisChar, Character prevChar) {
        if (prevChar == null) {
            return false;
        }
        if (EndOfLine.CARRIAGE_RERUN_LINE_FEED.equals(line.getEndOfLine())) {
            char first = EndOfLine.CARRIAGE_RERUN_LINE_FEED.getEolSymbol().charAt(0);
            char second = EndOfLine.CARRIAGE_RERUN_LINE_FEED.getEolSymbol().charAt(1);

            return first == prevChar && second == thisChar;
        }
        return false;
    }

    private static Charset getDetectedCharset(Map<String, Object> executionContext) {
        List<Charset> charsets = (List<Charset>) executionContext.get(POSSIBLE_CHARSETS);

        if (charsets == null || charsets.isEmpty()) {
            return null;
        }

        if (charsets.size() == 1) {
            return charsets.get(0);
        }

        if (charsets.equals(PluginCharsetDetector.US_ASCII_COMPATIBLE_CHARSETS)) {
            // we are okay with utf-8 here for parsing
            return Charset.UTF_8;
        }
        // there is no other way we get the list in detected charsets other than we have
        // US_ASCII_COMPATIBLE_CHARSETS
        return null;
    }

    private static void warnNoCharsetDetected(File source) {
        PluginConfiguration.getInstance()
                .<Log>getLog()
                .warn(
                        "We cannot check for the trim trailing whitespaces because we do not really now the charset being used for the file : "
                                + source.getName());
    }

    @Override
    public TrueFalse getValueFromSection(Section section) {
        return section.getTrimTrailingWhitespace();
    }

    @Override
    public int getOrder() {
        return LATEST;
    }
}
