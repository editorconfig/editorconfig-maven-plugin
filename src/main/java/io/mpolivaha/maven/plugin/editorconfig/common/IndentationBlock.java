package io.mpolivaha.maven.plugin.editorconfig.common;

/**
 * @param indentLevel level of indentation is used for this block
 */
public record IndentationBlock(
    int indentLevel
) {

  public static IndentationBlock root() {
    return new IndentationBlock(0);
  }

  public IndentationBlock next(int indentSize) {
    return new IndentationBlock(this.indentLevel + indentSize);
  }
}
