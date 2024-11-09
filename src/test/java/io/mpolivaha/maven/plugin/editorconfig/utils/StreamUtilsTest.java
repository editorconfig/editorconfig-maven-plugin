package io.mpolivaha.maven.plugin.editorconfig.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StreamUtils}
 *
 * @author Mikhail Polivakha
 */
class StreamUtilsTest {

  @Test
  void testForEachIndexed_happyPath() {
    Map<Integer, String> list = new HashMap<>();
    StreamUtils.forEachIndexed(Stream.of("one", "two", "three", "four"), (s, index) -> list.put(index, s));
    Assertions
        .assertThat(list)
        .containsExactlyEntriesOf(Map.of(1, "one", 2, "two", 3, "three", 4, "four"));
  }
}