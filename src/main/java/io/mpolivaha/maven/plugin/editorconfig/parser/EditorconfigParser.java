package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.GlobExpression;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.model.Editorconfig.Section.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.parser.ParsingUtils.KeyValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.maven.plugin.MojoExecutionException;

public class EditorconfigParser {

  private String editorconfigLocation;

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
          final var workaround = new LineNumberAndLine(line, lineNumber);
          KeyValue keyValue = ParsingUtils.parseKeyValue(line).orElseThrow(
              () ->
                  new MojoExecutionException(
                      "Line number '%d' with content : '%s' expected to contain key/value pair, but we cannot parse it".formatted(
                          workaround.lineNumber(),
                          workaround.line()
                      )
                  )
              );
          Option.from(keyValue.key()).orElseThrow();
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
