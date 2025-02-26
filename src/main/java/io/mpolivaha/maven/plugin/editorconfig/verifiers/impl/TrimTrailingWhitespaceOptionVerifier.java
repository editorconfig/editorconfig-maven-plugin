package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import static io.mpolivaha.maven.plugin.editorconfig.verifiers.context.ContextKeys.POSSIBLE_CHARSETS;

import io.mpolivaha.maven.plugin.editorconfig.model.Section;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.common.PluginCharsetDetector;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.logging.Log;

/**
 * {@link SpecOptionVerifier} for trim_trailing_whitespace
 *
 * @author Mikhail Polivakha
 * @see TrueFalse
 */
public class TrimTrailingWhitespaceOptionVerifier extends SpecOptionVerifier<TrueFalse> {

  public TrimTrailingWhitespaceOptionVerifier() {
    super(Option.TRIM_TRAILING_WHITESPACE);
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, TrueFalse optionValue, OptionValidationResult result, Map<String, Object> executionContext) {
    Charset charsetForParing = getDetectedCharset(executionContext);

    if (charsetForParing == null) {
      warnNoCharsetDetected();
      return;
    }

    InputStreamReader lineReader = new InputStreamReader(
        new ByteArrayInputStream(line.getContentWithEol()),
        charsetForParing.getJavaCharset()
    );

    Character thisCharacter;
    Character prevCharacter = null;
    boolean haveTrailingWhiteSpace = false;

    while ((thisCharacter = ExecutionUtils.mapExceptionally(() -> (char) lineReader.read())) != -1) {

      if (isSingleCharEOL(line, thisCharacter) || isMultiCharEol(line, thisCharacter, prevCharacter)) {
        if (haveTrailingWhiteSpace) {
          result.addErrorMessage("For the line number %d expected no trailing characters".formatted(lineNumber));
        }
        break;
      }

      haveTrailingWhiteSpace = Character.isWhitespace(thisCharacter);
      prevCharacter = thisCharacter;
    }
  }

  private static boolean isSingleCharEOL(ByteArrayLine line, char character) {
    return line.getEndOfLine().isSingleCharacter() &&
        line.getEndOfLine().getEolSymbol().charAt(0) == character;
  }

  private static boolean isMultiCharEol(ByteArrayLine line, char thisChar, Character prevChar) {
    if (prevChar == null) {
      return false;
    }
    if (EndOfLine.CARRIAGE_RERUN_LINE_FEED.equals(line.getEndOfLine())) {
      char first = EndOfLine.CARRIAGE_RERUN_LINE_FEED.getEolSymbol().charAt(0);
      char second = EndOfLine.CARRIAGE_RERUN_LINE_FEED.getEolSymbol().charAt(1);

      return first == prevChar && second == thisChar;
    }
    return false;
  }

  private static Charset getDetectedCharset(Map<String, Object> executionContext) {
    List<Charset> charsets = (List<Charset>) executionContext.get(POSSIBLE_CHARSETS);

    if (charsets == null || charsets.isEmpty()) {
      return null;
    }

    if (charsets.size() == 1) {
      return charsets.get(0);
    }

    if (charsets.equals(PluginCharsetDetector.US_ASCII_COMPATIBLE_CHARSETS)) {
      // we are okay with utf-8 here for parsing
      return Charset.UTF_8;
    }
    // there is no other way we get the list in detected charsets other than we have US_ASCII_COMPATIBLE_CHARSETS
    return null;
  }

  private static void warnNoCharsetDetected() {
    PluginConfiguration
        .getInstance()
        .<Log>getLog()
        .warn("We cannot check for the trim trailing whitespaces because we do not really now the charset being used for the file"); // TODO: add filename
  }

  @Override
  public TrueFalse getValueFromSection(Section section) {
    return section.getTrimTrailingWhitespace();
  }

  @Override
  public int getOrder() {
    return LATEST;
  }
}
