/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.List;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.common.PluginCharsetDetector;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.VerifiersExecutionContext;
import org.editorconfig.plugin.maven.verifiers.context.ContextKeys;
import org.jspecify.annotations.NonNull;

/**
 * {@link SpecOptionVerifier} for the {@link Charset}
 *
 * @author Mikhail Polivakha
 */
public class CharsetOptionVerifier extends SpecOptionVerifier<Charset> {

    private final PluginCharsetDetector pluginCharsetDetector;

    public CharsetOptionVerifier() {
        super(Option.CHARSET);
        this.pluginCharsetDetector = new PluginCharsetDetector();
    }

    @Override
    protected OptionValidationResult checkInternal(
            CachingInputStream content,
            Section section,
            VerifiersExecutionContext executionContext) {
        Charset expectedCharset = getValueFromSection(section);
        OptionValidationResult result = new OptionValidationResult(targetOption, expectedCharset);

        byte[] contentAsBytes = ExecutionUtils.mapExceptionally(content::readAllBytes);
        List<Charset> detectedCharsets = pluginCharsetDetector.detect(contentAsBytes);

        if (!detectedCharsets.isEmpty()) {
            executionContext.putGlobal(ContextKeys.POSSIBLE_CHARSETS, detectedCharsets);
        }

        if (detectedCharsets.stream().noneMatch(charset -> charset.equals(expectedCharset))) {
            result.addErrorMessage("Expected the file encoding : %s, but possible charsets are : %s"
                    .formatted(targetOption.name(), detectedCharsets));
        }
        return result;
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            Charset optionValue,
            OptionValidationResult result,
            @NonNull VerifiersExecutionContext executionContext) {}

    @Override
    public Charset getValueFromSection(Section section) {
        return section.getCharset();
    }

    @Override
    public int getOrder() {
        return EARLIEST;
    }
}
