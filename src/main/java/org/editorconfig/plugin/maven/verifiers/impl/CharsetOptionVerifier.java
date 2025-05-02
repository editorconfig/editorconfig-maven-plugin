/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.PluginCharsetDetector;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;

import org.editorconfig.plugin.maven.verifiers.context.ContextKeys;

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
            InputStream content, Section section, Map<String, Object> executionContext) {
        Charset expectedCharset = getValueFromSection(section);
        OptionValidationResult result = new OptionValidationResult(targetOption, expectedCharset);

        byte[] contentAsBytes = ExecutionUtils.mapExceptionally(content::readAllBytes);
        List<Charset> detectedCharsets = pluginCharsetDetector.detect(contentAsBytes);

        if (!detectedCharsets.isEmpty()) {
            executionContext.put(ContextKeys.POSSIBLE_CHARSETS, detectedCharsets);
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
            Map<String, Object> executionContext) {}

    @Override
    public Charset getValueFromSection(Section section) {
        return section.getCharset();
    }

    @Override
    public int getOrder() {
        return EARLIEST;
    }
}
