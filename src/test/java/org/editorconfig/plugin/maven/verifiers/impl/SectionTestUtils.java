/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.function.Consumer;

import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.GlobExpression;
import org.editorconfig.plugin.maven.model.IndentationStyle;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.SectionBuilder;
import org.editorconfig.plugin.maven.utils.IntegerUtils;

public class SectionTestUtils {

    public static Section testSection(Consumer<SectionBuilder> modifier) {
        SectionBuilder sectionBuilder = new SectionBuilder(GlobExpression.from("[*]"))
                .endOfLine(OptionValue.resolve("lf", EndOfLine::from))
                .charset(OptionValue.resolve("utf-8", Charset::from))
                .indentationSize(OptionValue.resolve("2", IntegerUtils::parseIntSafe))
                .indentationStyle(OptionValue.resolve("tab", IndentationStyle::from));
        modifier.accept(sectionBuilder);
        return sectionBuilder.completeSection();
    }
}
