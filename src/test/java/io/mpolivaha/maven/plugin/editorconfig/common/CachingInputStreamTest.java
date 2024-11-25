package io.mpolivaha.maven.plugin.editorconfig.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CachingInputStreamTest {

  @Test
  void test_readAllBytes_twice() throws IOException {
    CachingInputStream cachingInputStream = fromFile("caching_input_stream/SomeClass.java");

    byte[] first = cachingInputStream.readAllBytes();
    cachingInputStream.reset();
    byte[] second = cachingInputStream.readAllBytes();

    Assertions.assertThat(first).isEqualTo(second);
  }

  @Test
  void test_BufferedInputStream() throws IOException {
    CachingInputStream cachingInputStream = fromFile("caching_input_stream/SimpleRecord.java");

    BufferedInputStream bufferedInputStream = new BufferedInputStream(cachingInputStream);
    ByteArrayOutputStream firstIteration = new ByteArrayOutputStream();
    ByteArrayOutputStream secondIteration = new ByteArrayOutputStream();

    ByteArrayLine line;

    while (!(line = bufferedInputStream.readLine()).isTheLastLine()) {
      firstIteration.writeBytes(line.getContentWithEol());
    }

    bufferedInputStream.reset();

    while (!(line = bufferedInputStream.readLine()).isTheLastLine()) {
      secondIteration.writeBytes(line.getContentWithEol());
    }

    Assertions.assertThat(firstIteration.toByteArray()).isEqualTo(secondIteration.toByteArray());
  }

  private static CachingInputStream fromFile(String file) {
    try {
      return new CachingInputStream(new File(ClassLoader.getSystemClassLoader().getResource(file).toURI()));
    } catch (FileNotFoundException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}