package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import static io.mpolivaha.maven.plugin.editorconfig.verifiers.context.ContextKeys.POSSIBLE_CHARSETS;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import io.mpolivaha.maven.plugin.editorconfig.common.ByteArrayLine;
import io.mpolivaha.maven.plugin.editorconfig.common.PluginCharsetDetector;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.SpecOptionVerifier;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.maven.plugin.logging.Log;
import org.mozilla.universalchardet.CharsetListener;
import org.mozilla.universalchardet.UniversalDetector;
import org.mozilla.universalchardet.prober.UTF8Prober;

/**
 * {@link SpecOptionVerifier} for the {@link Charset}
 *
 * @author Mikhail Polivakha
 */
public class CharsetOptionVerifier extends SpecOptionVerifier<Charset> {

  private final PluginCharsetDetector pluginCharsetDetector;

  public CharsetOptionVerifier(Option targetOption) {
    super(targetOption);
    this.pluginCharsetDetector = new PluginCharsetDetector();
  }

  @Override
  protected OptionValidationResult checkInternal(InputStream content, Section section, Map<String, Object> executionContext) {
    Charset expectedCharset = getValueFromSection(section);
    OptionValidationResult result = new OptionValidationResult(targetOption, expectedCharset);

    byte[] contentAsBytes = ExecutionUtils.mapExceptionally(content::readAllBytes);
    List<Charset> detectedCharsets = pluginCharsetDetector.detect(contentAsBytes);

    if (!detectedCharsets.isEmpty()) {
      executionContext.put(POSSIBLE_CHARSETS, detectedCharsets);
    }

    if (detectedCharsets.stream().noneMatch(charset -> charset.equals(expectedCharset))) {
      result.addErrorMessage("Expected the file encoding : %s, but possible charsets are : %s".formatted(targetOption.name(), detectedCharsets));
    }
    return result;
  }

  @Override
  protected void forEachLine(ByteArrayLine line, int lineNumber, Charset optionValue, OptionValidationResult result, Map<String, Object> executionContext) {}

  @Override
  public Charset getValueFromSection(Section section) {
    return section.getCharset();
  }

  @Override
  public int getOrder() {
    return EARLIEST;
  }
}
