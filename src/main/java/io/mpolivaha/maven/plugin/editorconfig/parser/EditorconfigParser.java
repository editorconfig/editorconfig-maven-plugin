package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.common.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.GlobExpression;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.parser.ParsingUtils.KeyValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.maven.plugin.MojoExecutionException;

public class EditorconfigParser {

  private String editorconfigLocation;

  private static final BiFunction<String, Integer, String> KEY_VALUE_PARSE_ERROR = (line, lineNumber) ->
      "For line number '%d' with content : '%s' expected to contain key/value pair, but we cannot parse it".formatted(
          lineNumber,
          line
      );

  private static final BiFunction<String, Integer, String> UNRECOGNIZED_KEY_ERROR = (line, lineNumber) ->
      "For line number '%d' with content : '%s' parsed the key : '%s', which is not among the recognized keys : %s".formatted(
          lineNumber,
          line,
          Arrays.toString(Option.values())
      );

  public EditorconfigParser(String editorconfigLocation) {
    InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(editorconfigLocation);
    this.editorconfigLocation = editorconfigLocation;
  }

  public void init(InputStream resourceAsStream) throws MojoExecutionException {
    try (var reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
      parseInternally(reader);
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to read .editorconfig file in '%s'".formatted(editorconfigLocation), e);
    }
  }

  private static void parseInternally(BufferedReader reader) throws IOException, MojoExecutionException {
    String line;
    int lineNumber = 0;
    SectionBuilder builder = Section.builder();

    while ((line = reader.readLine()) != null) {
      lineNumber++;

      if (line.isBlank()) {
        continue;
      }

      if (!ParsingUtils.isComment(line)) {
        if (ParsingUtils.isSection(line)) {
          builder.globExpression(GlobExpression.from(line.trim()));
        } else {
          final var holder = new LineNumberAndLine(line, lineNumber); // see javadoc on holder class
          Optional<KeyValue> keyValue = ParsingUtils.parseKeyValue(line);

          if (keyValue.isEmpty()) {
            ExecutionUtils.handleError(KEY_VALUE_PARSE_ERROR.apply(holder.line, holder.lineNumber));
            continue;
          }

          Optional<Option> option = Option.from(keyValue.get().key());

          if (option.isEmpty()) {
            ExecutionUtils.handleError(UNRECOGNIZED_KEY_ERROR.apply(holder.line, holder.lineNumber));
            continue;
          }

          ;
        }
      }
    }
  }

  /**
   * This class mainly exists due to limitations we have in use of local variables in lambda (effectively final constraint)
   * @param line
   * @param lineNumber
   */
  private static record LineNumberAndLine(String line, int lineNumber) {}
}
