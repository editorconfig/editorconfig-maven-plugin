/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.Map;

import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.IndentationBlock;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.TrueFalse;
import org.editorconfig.plugin.maven.verifiers.OptionValidationResult;
import org.editorconfig.plugin.maven.verifiers.SpecOptionVerifier;

/**
 * {@link SpecOptionVerifier} for indentation_size option
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class IndentationSizeOptionVerifier extends SpecOptionVerifier<Integer> {

    private Section section;

    private static final String PREVIOUS_LINE_KEY = "PREVIOUS_LINE_KEY";

    /**
     * The indentation block that is applicable for the given line
     */
    private static final String INDENTATION_BLOCK = "INDENTATION_BLOCK";

    public IndentationSizeOptionVerifier() {
        super(Option.IDENT_SIZE);
    }

    @Override
    protected void onInit(Section section) {
        this.section = section;
    }

    @Override
    protected void forEachLine(
            ByteArrayLine line,
            int lineNumber,
            Integer optionValue,
            OptionValidationResult result,
            Map<String, Object> executionContext) {
        Assert.notNull(section, "The section cannot be null at this point");
        var currentIndentationBlock = (IndentationBlock)
                executionContext.getOrDefault(INDENTATION_BLOCK, IndentationBlock.root());

        // TODO:
        if (!line.isEmpty()) {
            //        line.getIndentInColumns()
        }

        //    executionContext.put(
        //        PREVIOUS_LINE_KEY,
        //        discoverIndentationBlock(line, optionValue, currentIndentationBlock)
        //    );
    }

    @Override
    public Integer getValueFromSection(Section section) {
        return section.getIndentationSize();
    }
}
