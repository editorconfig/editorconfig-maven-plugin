package io.mpolivaha.maven.plugin.editorconfig;

import io.mpolivaha.maven.plugin.editorconfig.model.Charset;
import io.mpolivaha.maven.plugin.editorconfig.model.EndOfLine;
import io.mpolivaha.maven.plugin.editorconfig.model.IndentationStyle;
import io.mpolivaha.maven.plugin.editorconfig.model.TrueFalse;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * .editorconfig file representation
 *
 * @author Mikhail Polivakha
 */
public class Editorconfig {

  private boolean isRoot = false;

  private Path location;

  private List<Section> sections;

  public Editorconfig addSection(Section section) {
    if (this.sections == null) {
      this.sections = new LinkedList<>();
    }
    this.sections.add(section);
    return this;
  }

  public Editorconfig markAsRoot() {
    this.isRoot = true;
    return this;
  }

  public boolean isRoot() {
    return this.isRoot;
  }

  public List<Section> getSections() {
    return sections;
  }

  public Path getLocation() {
    return location;
  }

  public Editorconfig setLocation(Path location) {
    this.location = location;
    return this;
  }

  public Optional<Section> findTargetSection(Path file) {
    return this.sections.stream().filter(section -> section.accepts(file)).findFirst();
  }

  /**
   * Corresponds to a particular section of the .editorconfig file
   * <p>
   * This class is <strong>intentionally</strong> not static, as it is, by design, is created
   * within the {@link Editorconfig}. There might be an option to just composite the {@link Editorconfig}
   * instance within {@link Section current section}, but the design decicion was made to try to solve this
   * via java inner classes.
   */
  public class Section {

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

    public String getLocation() {
      return location;
    }

    public IndentationStyle getIndentationStyle() {
      return indentationStyle;
    }

    public GlobExpression getGlobExpression() {
      return globExpression;
    }

    public EndOfLine getEndOfLine() {
      return endOfLine;
    }

    public Charset getCharset() {
      return charset;
    }

    public TrueFalse getTrimTrailingWhitespace() {
      return trimTrailingWhitespace;
    }

    public TrueFalse getInsertFinalNewLine() {
      return insertFinalNewLine;
    }

    public boolean accepts(Path file) {
      return this.globExpression.accepts(file);
    }

    public static SectionBuilder builder() {
      return new Editorconfig().new SectionBuilder();
    }
  }

  /**
   * Builder for {@link Section section}.
   *
   * <p>
   * This class is <strong>intentionally</strong> not static, as it is, by design, is created
   * within the {@link Editorconfig}. There might be an option to just composite the {@link Editorconfig}
   * instance within {@link Section current section}, but the design decicion was made to try to solve this
   * via java inner classes.
   */
  public class SectionBuilder {

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

    public GlobExpression getGlobExpression() {
      return this.globExpression;
    }

    /**
     * Obtain the enclosing editroconfig
     */
    public Editorconfig getEditorconfig() {
      return Editorconfig.this;
    }

    /**
     * Build section withing {@link Editorconfig editorconfig}, that is the enclosing for this {@link SectionBuilder builder}.
     */
    public Editorconfig build() {
      Editorconfig editorconfig = getEditorconfig();
      editorconfig.addSection(
          new Section(location, indentationStyle, globExpression, endOfLine, charset, trimTrailingWhitespace, insertFinalNewLine)
      );
      return editorconfig;
    }
  }


  /**
   * Glob expression of the particular {@link Section}
   */
  public static class GlobExpression {

    private final String raw;

    private GlobExpression(String raw) {
      this.raw = raw;
    }

    public String getRaw() {
      return raw;
    }

    public static GlobExpression from(String raw) {
      return new GlobExpression(raw);
    }

    private boolean accepts(Path path) {
      throw new UnsupportedOperationException();
    }
  }
}
