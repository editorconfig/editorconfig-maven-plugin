/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import java.util.function.Consumer;

import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.GlobExpression;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.Section;

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
