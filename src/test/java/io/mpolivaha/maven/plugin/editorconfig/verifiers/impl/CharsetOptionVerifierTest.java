package io.mpolivaha.maven.plugin.editorconfig.verifiers.impl;

import io.mpolivaha.maven.plugin.editorconfig.common.CachingInputStream;
import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import io.mpolivaha.maven.plugin.editorconfig.verifiers.OptionValidationResult;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link CharsetOptionVerifier}
 *
 * @author Mikhail Polivakha
 */
class CharsetOptionVerifierTest {

  private final CharsetOptionVerifier subject = new CharsetOptionVerifier(Option.CHARSET);

  @ParameterizedTest
  @MethodSource(value = {"arguments"})
  void testCharsetOptionVerifier(String sourceCodeFile, Charset charset, boolean noErrors) throws Exception {
    OptionValidationResult check = subject.check(
        new CachingInputStream(
            Paths.get(ClassLoader
                .getSystemClassLoader()
                .getResource(sourceCodeFile).toURI()).toFile()
        ),
        SectionTestUtils.testSection(sectionBuilder -> sectionBuilder.charset(charset))
    );

    Assertions.assertThat(check.noErrors()).isEqualTo(noErrors);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("sources/charset/TestJavaClass_LATIN1.java", Charset.LATIN1, true),
        Arguments.of("sources/charset/TestJavaClass_LATIN1.java", Charset.UTF_8, false),
        Arguments.of("sources/charset/TestJavaClass_UTF8.java", Charset.UTF_8, true),
        Arguments.of("sources/charset/TestJavaClass_UTF8.java", Charset.UTF_16LE, false),
        Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", Charset.UTF_16BE, true),
        Arguments.of("sources/charset/TestJavaClass_UTF16BE.java", Charset.UTF_16LE, false),
        Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", Charset.UTF_16LE, true),
        Arguments.of("sources/charset/TestJavaClass_UTF16LE.java", Charset.LATIN1, false)
    );
  }
}