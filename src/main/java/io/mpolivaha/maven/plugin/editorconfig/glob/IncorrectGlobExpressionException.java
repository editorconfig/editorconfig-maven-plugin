package io.mpolivaha.maven.plugin.editorconfig.glob;

/**
 * {@link RuntimeException} that is thrown by {@link GlobExpressionParser} in case the glob expression is not valid.
 *
 * @author Mikhail Polivakha
 */
public class IncorrectGlobExpressionException extends RuntimeException {

  public IncorrectGlobExpressionException(String expression, String reason) {
    super("The glob expression : %s is not correct. Reason : %s".formatted(expression, reason));
  }
}
