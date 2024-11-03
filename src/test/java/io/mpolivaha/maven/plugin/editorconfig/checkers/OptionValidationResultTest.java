package io.mpolivaha.maven.plugin.editorconfig.checkers;

import io.mpolivaha.maven.plugin.editorconfig.model.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OptionValidationResult}
 *
 * @author Mikhail Polivakha
 */
class OptionValidationResultTest {

  @Test
  void testViolationCount() {
    // given
    var first = new OptionValidationResult(Option.CHARSET, "utf-8");
    var second = new OptionValidationResult(Option.CHARSET, "utf-8");

    // when
    second.addErrorMessage("Some error message!");

    // then
    Assertions.assertThat(first.violationsCount()).isEqualTo(0);
    Assertions.assertThat(second.violationsCount()).isEqualTo(1);
  }

  @Test
  void testNoErrors() {
    // given
    var first = new OptionValidationResult(Option.CHARSET, "utf-8");
    var second = new OptionValidationResult(Option.CHARSET, "utf-8");

    // when
    second.addErrorMessage("Some error message!");

    // then
    Assertions.assertThat(first.noErrors()).isEqualTo(true);
    Assertions.assertThat(second.noErrors()).isEqualTo(false);
  }

  @Test
  void testProfileRendering() {
    // given
    var first = new OptionValidationResult(Option.CHARSET, "utf-8");
    first.addErrorMessage("First Error!");
    first.addErrorMessage("Second Error!");

    // when
    String errorMessage = first.renderErrorMessage();

    // then
    Assertions.assertThat(first.noErrors()).isEqualTo(false);
    Assertions.assertThat(first.violationsCount()).isEqualTo(2);
    Assertions.assertThat(errorMessage).isEqualTo(
        "For option charset=utf-8 found 2 violation(-s):\n"
        + "\t- First Error!\n"
        + "\t- Second Error!\n"
    );
  }
}