package io.mpolivaha.maven.plugin.editorconfig.parser;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link EditorconfigParser}
 *
 * @author Mikhail Polivakha
 */
class EditorconfigParserTest {

  @Test
  void testFileParsing() {
    EditorconfigParser editorconfigParser = new EditorconfigParser();
//    editorconfigParser.parse();
  }
}