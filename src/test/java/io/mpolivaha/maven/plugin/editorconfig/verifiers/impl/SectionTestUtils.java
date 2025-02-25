package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import java.util.function.Consumer;

public class SectionTestUtils {

  public static Section testSection() {
    return testSection(sectionBuilder -> {});
  }

  public static Section testSection(Consumer<SectionBuilder> modifier) {
    SectionBuilder sectionBuilder = Section
        .builder()
        .endOfLine(EndOfLine.LINE_FEED)
        .charset(Charset.UTF_8)
        .indentationSize(2)
        .indentationStyle(IndentationStyle.TAB);
    modifier.accept(sectionBuilder);
    return sectionBuilder.completeSection().getSections().get(0);
  }
}
