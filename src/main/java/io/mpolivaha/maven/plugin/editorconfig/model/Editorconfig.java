package io.mpolivaha.maven.plugin.editorconfig.model;

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
    private String location;

    private IndentationStyle indentationStyle;

    private GlobExpression globExpression;

    public Section(String location, IndentationStyle indentationStyle, GlobExpression globExpression) {
      this.location = location;
      this.indentationStyle = indentationStyle;
      this.globExpression = globExpression;
    }

    public static SectionBuilder builder() {
      return new SectionBuilder();
    }

    public static class SectionBuilder {

      private String location;
      private IndentationStyle indentationStyle;
      private GlobExpression globExpression;

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

      public Section build() {
        return new Section(location, indentationStyle, globExpression);
      }
    }
  }

  /**
   * Glob expression of the particular {@link Section}
   */
  public static class GlobExpression {

    private String raw;

    public static GlobExpression from(String raw) {

    }

    private boolean accepts(Path path) {
      throw new UnsupportedOperationException();
    }
  }
}
