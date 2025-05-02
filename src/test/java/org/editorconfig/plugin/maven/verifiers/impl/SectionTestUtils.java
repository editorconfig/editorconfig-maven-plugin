/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers.impl;

import java.util.function.Consumer;

import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.model.Editorconfig.SectionBuilder;
import org.editorconfig.plugin.maven.model.EndOfLine;
import org.editorconfig.plugin.maven.model.GlobExpression;
import org.editorconfig.plugin.maven.model.IndentationStyle;

public class SectionTestUtils {

    public static Section testSection() {
        return testSection(sectionBuilder -> {});
    }

    public static Section testSection(Consumer<SectionBuilder> modifier) {
        SectionBuilder sectionBuilder = new Editorconfig()
                .new SectionBuilder(GlobExpression.from("[*]"))
                .endOfLine(EndOfLine.LINE_FEED)
                .charset(Charset.UTF_8)
                .indentationSize(2)
                .indentationStyle(IndentationStyle.TAB);
        modifier.accept(sectionBuilder);
        return sectionBuilder.completeSection().getSections().get(0);
    }
}
