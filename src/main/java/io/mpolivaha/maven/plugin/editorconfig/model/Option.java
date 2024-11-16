package io.mpolivaha.maven.plugin.editorconfig.model;

import static io.mpolivaha.maven.plugin.editorconfig.utils.OptionUtils.ERROR_MESSAGE;

import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.Section;
import io.mpolivaha.maven.plugin.editorconfig.Editorconfig.SectionBuilder;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExecutionUtils;
import io.mpolivaha.maven.plugin.editorconfig.utils.IntegerUtils;
import io.mpolivaha.maven.plugin.editorconfig.utils.OptionUtils;
import io.mpolivaha.maven.plugin.editorconfig.utils.ParsingUtils.KeyValue;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Possible options that can be specified inside a single {@link Section section}.
 *
 * @author Mikhail Polivakha
 */
public enum Option {

  IDENT_STYLE(
      OptionUtils.INDENT_STYLE,
      (keyValue, sectionBuilder) ->
          assign(
            IndentationStyle.from(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.INDENT_STYLE, keyValue.value(), toStringArr(IndentationStyle.values())),
            sectionBuilder::indentationStyle
          )
  ),
  IDENT_SIZE(
      OptionUtils.INDENT_SIZE,
      (keyValue, sectionBuilder) -> {
        assign(
            IntegerUtils.parseIntSafe(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.INDENT_SIZE, keyValue.value(), new String[]{"1", "2", "3", "4", "5", "..."}),
            sectionBuilder::indentationSize
        );
      }
  ),
  TAB_WIDTH(
      OptionUtils.TAB_WIDTH,
      (keyValue, sectionBuilder) -> {
        assign(
            IntegerUtils.parseIntSafe(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.TAB_WIDTH, keyValue.value(), new String[]{"1", "2", "3", "4", "5", "..."}),
            sectionBuilder::tabWidth
        );
      }
  ),
  END_OF_LINE(
      OptionUtils.END_OF_LINE,
      (keyValue, sectionBuilder) -> {
        assign(
            EndOfLine.from(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.END_OF_LINE, keyValue.value(), toStringArr(EndOfLine.values())),
            sectionBuilder::endOfLine
        );
      }
  ),
  CHARSET(
      OptionUtils.CHARSET,
      (keyValue, sectionBuilder) -> {
        assign(
            Charset.from(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.CHARSET, keyValue.value(), toStringArr(Charset.values())),
            sectionBuilder::charset
        );
      }
  ),
  TRIM_TRAILING_WHITESPACE(
      OptionUtils.TRIM_TRAILING_WHITESPACE,
      (keyValue, sectionBuilder) -> {
        assign(
            TrueFalse.from(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.TRIM_TRAILING_WHITESPACE, keyValue.value(), toStringArr(TrueFalse.values())),
            sectionBuilder::trimTrailingWhitespace
        );
      }
  ),
  INSERT_FINAL_NEW_LINE(
      OptionUtils.INSERT_FINAL_NEW_LINE,
      (keyValue, sectionBuilder) -> {
        assign(
            TrueFalse.from(keyValue.value()),
            ERROR_MESSAGE.apply(OptionUtils.INSERT_FINAL_NEW_LINE, keyValue.value(), toStringArr(TrueFalse.values())),
            sectionBuilder::insertFinalNewLine
        );
      }
  );

  private final String key;

  /**
   * Represent the merge function that represent a way to merge the parsed {@link KeyValue key-value pair}
   * into the current {@link SectionBuilder section} of the editorconfig that we're currently processing
   */
  private final BiConsumer<KeyValue, SectionBuilder> mergeFunction;

  Option(String key, BiConsumer<KeyValue, SectionBuilder> mutator) {
    this.key = key;
    this.mergeFunction = mutator;
  }

  public void merge(KeyValue keyValue, SectionBuilder sectionBuilder) {
    this.mergeFunction.accept(keyValue, sectionBuilder);
  }

  public static Optional<Option> from(String key) {
    for (Option value : values()) {
      if (value.key.equalsIgnoreCase(key)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  public String getKey() {
    return key;
  }

  private static <T> void assign(
      Optional<T> source,
      String errorMessage,
      Consumer<T> assigner
      ) {
    if (source.isEmpty()) {
      ExecutionUtils.handleError(errorMessage);
    } else {
      assigner.accept(source.get());
    }
  }

  private static <T extends Enum<T>> String[] toStringArr(T[] values) {
    return Arrays.stream(values).map(Enum::name).toArray(String[]::new);
  }
}
