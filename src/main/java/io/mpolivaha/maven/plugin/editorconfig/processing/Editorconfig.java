package io.mpolivaha.maven.plugin.editorconfig.processing;

import java.nio.file.Path;
import java.util.List;

/**
 * .editorconfig file representation
 *
 * @author Mikhail Polivakha
 */
public class Editorconfig {

  private List<Section> rules;

  /**
   * Corresponds to a particular section of the .editorconfig file
   */
  private static class Section {

    /**
     * Location of .editorconfig file relative to the classpath root
     */
    private String location;

    private IndentationStyle indentationStyle;

    private Boolean isRoot;

    private GlobExpression globExpression;
  }

  /**
   * Glob expression of the particular {@link Section}
   */
  private static class GlobExpression {

    private String raw;

    private boolean accepts(Path path) {
      throw new UnsupportedOperationException();
    }
  }
}
