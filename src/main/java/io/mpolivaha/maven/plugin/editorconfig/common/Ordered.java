package io.mpolivaha.maven.plugin.editorconfig.common;

/**
 * Something that can be ordered.
 *
 * @author Mikhail Polivakha
 */
public interface Ordered {

  int EARLIEST = Integer.MIN_VALUE;
  int LATEST = Integer.MAX_VALUE;
  int NORMAL = 0;

  int getOrder();
}
