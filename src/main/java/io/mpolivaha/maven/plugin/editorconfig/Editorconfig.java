package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * .editorconfig file representation
 *
 * @author Mikhail Polivakha
 */
public class Editorconfig {

  private boolean isRoot = false;

  private List<Section> sections;

  public Editorconfig addSection(Section section) {
    if (this.sections == null) {
      this.sections = new LinkedList<>();
    }
    this.sections.add(section);
    return this;
  }

  public Editorconfig setRoot() {
    this.isRoot = true;
    return this;
  }

  /**
   * Corresponds to a particular section of the .editorconfig file
   */
  public static class Section {

    /**
     * Location of .editorconfig file relative to the classpath root
     */
    private final String location;

    private final IndentationStyle indentationStyle;

    private final GlobExpression globExpression;

    private final EndOfLine endOfLine;

    private final Charset charset;

    private final TrueFalse trimTrailingWhitespace;

    private final TrueFalse insertFinalNewLine;

    public Section(String location, IndentationStyle indentationStyle, GlobExpression globExpression, EndOfLine endOfLine, Charset charset,
        TrueFalse trimTrailingWhitespace, TrueFalse insertFinalNewLine) {
      this.location = location;
      this.indentationStyle = indentationStyle;
      this.globExpression = globExpression;
      this.endOfLine = endOfLine;
      this.charset = charset;
      this.trimTrailingWhitespace = trimTrailingWhitespace;
      this.insertFinalNewLine = insertFinalNewLine;
    }

    public static SectionBuilder builder() {
      return new SectionBuilder();
    }

    public static class SectionBuilder {

      private String location;
      private IndentationStyle indentationStyle;
      private GlobExpression globExpression;
      private EndOfLine endOfLine;
      private Charset charset;
      private TrueFalse trimTrailingWhitespace;

      private TrueFalse insertFinalNewLine;

      public SectionBuilder location(String location) {
        this.location = location;
        return this;
      }

      public SectionBuilder indentationStyle(IndentationStyle indentationStyle) {
        this.indentationStyle = indentationStyle;
        return this;
      }

      public SectionBuilder globExpression(GlobExpression globExpression) {
        this.globExpression = globExpression;
        return this;
      }

      public SectionBuilder endOfLine(EndOfLine endOfLine) {
        this.endOfLine = endOfLine;
        return this;
      }

      public SectionBuilder charset(Charset charset) {
        this.charset = charset;
        return this;
      }

      public SectionBuilder trimTrailingWhitespace(TrueFalse trimTrailingWhitespace) {
        this.trimTrailingWhitespace = trimTrailingWhitespace;
        return this;
      }

      public SectionBuilder insertFinalNewLine(TrueFalse insertFinalNewLine) {
        this.insertFinalNewLine = insertFinalNewLine;
        return this;
      }

      public Section build() {
        return new Section(location, indentationStyle, globExpression, endOfLine, charset, trimTrailingWhitespace, insertFinalNewLine);
      }
    }
  }

  /**
   * Glob expression of the particular {@link Section}
   */
  public static class GlobExpression {

    private String raw;

    public static GlobExpression from(String raw) {
      throw new UnsupportedOperationException();
    }

    private boolean accepts(Path path) {
      throw new UnsupportedOperationException();
    }
  }
}
