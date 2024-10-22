package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration.Param;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link EditorconfigParser}
 *
 * @author Mikhail Polivakha
 */
class EditorconfigParserTest {

  @Test
  void testFileParsing() {
    PluginConfiguration.buildInstance(Map.of(Param.STRICT_MODE, true));
    EditorconfigParser editorconfigParser = new EditorconfigParser();
    editorconfigParser.parse("test-files/.root-editorconfig");
  }
}