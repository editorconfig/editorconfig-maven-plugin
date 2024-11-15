package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.BufferedInputStream;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * {@link SpecOptionVerifier} for the insert_final_new_line option
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class InsertFinalNewLineOptionVerifier extends SpecOptionVerifier<TrueFalse>{

  protected InsertFinalNewLineOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  OptionValidationResult checkInternal(InputStream content, TrueFalse optionValue) {
    try (var buffer = new BufferedInputStream(content)) {

      ByteArrayLine line;

      while (!(line = buffer.readLine()).isTheLastLine()) {
        // iterate until the last line of file is reached
      }

      OptionValidationResult result = new OptionValidationResult(targetOption, optionValue);

      if (Objects.equals(line.getEndOfLine(), EndOfLine.EOF)) {
         result.addErrorMessage("Expected the end_of_line symbol to be present, but file terminates with EOF");
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public TrueFalse getValueFromSection(Section section) {
    return section.getInsertFinalNewLine();
  }
}
