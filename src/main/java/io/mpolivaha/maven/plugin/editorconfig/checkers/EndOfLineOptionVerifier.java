package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.utils.StreamUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EndOfLineOptionVerifier extends SpecOptionVerifier<EndOfLine> {

  protected EndOfLineOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  OptionValidationResult checkInternal(InputStream content, EndOfLine optionValue) {
    try (var buffer = new BufferedReader(new InputStreamReader(content))) {
      StreamUtils.forEachIndexed(buffer.lines(), (row, rowNumber) -> {

      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public EndOfLine getValueFromSection(Section section) {
    return section.getEndOfLine();
  }
}
