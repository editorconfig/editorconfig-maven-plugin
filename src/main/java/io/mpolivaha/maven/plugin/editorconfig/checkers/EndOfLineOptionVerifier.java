package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.BufferedInputStream;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.common.TripleFunction;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.utils.StreamUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * {@link SpecOptionVerifier} for {@link EndOfLine} option
 *
 * @author Mikhail Polivakha
 */
public class EndOfLineOptionVerifier extends SpecOptionVerifier<EndOfLine> {

  // TODO: this might be a good idea to introduce the common abstraction over here and inherit error message
  // that is below from it, so the message will be shared across SpecOptionVerifier
  private static final TripleFunction<Integer, String, String, String> ERROR_MESSAGE_PRODUCER = (lienNum, expected, actual) ->
      "In line %d expected %s, but was %s".formatted(lienNum, expected, actual);

  protected EndOfLineOptionVerifier(Option targetOption) {
    super(targetOption);
  }

  @Override
  OptionValidationResult checkInternal(InputStream content, EndOfLine optionValue) {
    // TODO: yeahm this try/catch can be moved to the separate abstraction
    try (var buffer = new BufferedInputStream(content)) {
      int lineNumber = 1;
      ByteArrayLine line;
      OptionValidationResult result = new OptionValidationResult(Option.END_OF_LINE, optionValue);

      do {
        line = buffer.readLine();
        if (!Objects.equals(optionValue, line.getEndOfLine())) {
          // here, we're potentially checking the last line which might be EOF
          // In this case, we'll 100% will fail the build, and I think it is the
          // right way to do this since the last line of the file is still a POSIX line
          result.addErrorMessage(ERROR_MESSAGE_PRODUCER.apply(lineNumber, optionValue.getEolSymbol(), line.getEndOfLine().getEolSymbol()));
          lineNumber++;
        }
      } while (!line.isTheLastLine());

    } catch (IOException e) {
      // TODO add exception handling
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public EndOfLine getValueFromSection(Section section) {
    return section.getEndOfLine();
  }
}
