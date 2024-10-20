package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.GlobExpression;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
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

  private final String editorconfigLocation;

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
    init(resourceAsStream);
  }

  public void init(InputStream resourceAsStream) {
    try (var reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
      parseInternally(reader);
    } catch (IOException e) {
      Assert.sneakyThrows(new MojoExecutionException("Unable to read .editorconfig file in '%s'".formatted(editorconfigLocation), e));
    }
  }

  private static void parseInternally(BufferedReader reader) throws IOException {
    String line;
    ParingContext context = null;

    do {
      line = reader.readLine();
      if (line == null) {
        break;
      }
      if (context == null) {
        context = new ParingContext(line, Section.builder());
      } else {
        context.newline(line);
      }

      parseLineInternally(context);
    }
    while (true);
  }

  private static void parseLineInternally(ParingContext context) {
    String line = context.getLine();

    if (line.isBlank()) {
      return;
    }

    if (!ParsingUtils.isComment(line)) {
      parseSignificantLine(context);
    }
  }

  private static void parseSignificantLine(ParingContext context) {
    String line = context.getLine();

    if (ParsingUtils.isSection(line)) {
      context.getSectionBuilder().globExpression(GlobExpression.from(line.trim()));
    } else {
      final var holder = new LineNumberAndLine(line, context.getLineNumber()); // see javadoc on holder class
      Optional<KeyValue> optKeyValue = ParsingUtils.parseKeyValue(line);

      if (optKeyValue.isEmpty()) {
        ExecutionUtils.handleError(KEY_VALUE_PARSE_ERROR.apply(holder.line, holder.lineNumber));
        return;
      }

      KeyValue keyValue = optKeyValue.get();

      Optional<Option> option = Option.from(keyValue.key());

      if (option.isEmpty()) {
        checkForRoot(context, holder, keyValue);
        return;
      }

      option.get().merge(keyValue, context.getSectionBuilder());
    }
  }

  private static void checkForRoot(ParingContext context, LineNumberAndLine holder, KeyValue keyValue) {
    if (keyValue.key().equalsIgnoreCase("root")) {
      context.getSectionBuilder().getEditorconfig().markAsRoot();
    } else {
      ExecutionUtils.handleError(UNRECOGNIZED_KEY_ERROR.apply(holder.line, holder.lineNumber));
    }
  }

  /**
   * This class mainly exists due to limitations we have in use of local variables in lambda (effectively final constraint)
   * @param line
   * @param lineNumber
   */
  private record LineNumberAndLine(String line, int lineNumber) {}
}
