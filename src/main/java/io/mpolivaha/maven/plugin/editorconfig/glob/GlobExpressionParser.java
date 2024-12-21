package io.mpolivaha.maven.plugin.editorconfig.glob;

import io.mpolivaha.maven.plugin.editorconfig.annotations.ThreadSafe;
import io.mpolivaha.maven.plugin.editorconfig.assertions.Assert;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.regex.PatternSyntaxException;

/**
 * Parser of Glob expressions.
 *
 * @author Mikhail Polivakha
 */
@ThreadSafe
public class GlobExpressionParser {

  private final String editorConfigDirectory;

  public GlobExpressionParser(String editorConfigLocation) {
    Assert.notNull(editorConfigLocation, "Editorconfig file location cannot be null");
    this.editorConfigDirectory = editorConfigLocation.substring(0, editorConfigLocation.lastIndexOf(File.separator));
  }

  /**
   * Can accept only individual
   *
   * @param globExpression - glob expression to check
   * @return true if the passed {@link Path} fits into #globExpression
   */
  public boolean accepts(Path path, String globExpression) {
    String absolutePath = path.normalize().toFile().getAbsolutePath();

    if (absolutePath.startsWith(editorConfigDirectory)) {
      Path truncated = Paths.get(absolutePath.substring(editorConfigDirectory.length() + 1));
      return acceptsInternal(truncated, globExpression);
    } else {
      return false;
    }
  }

  private static boolean acceptsInternal(Path path, String globExpression) {
    try {
      FileSystem fs = FileSystems.getDefault();
      PathMatcher pathMatcher = fs.getPathMatcher("glob:" + globExpression);
      return pathMatcher.matches(path);
    } catch (PatternSyntaxException exception) {
      throw new IncorrectGlobExpressionException(globExpression, exception.getMessage());
    }
  }
}
