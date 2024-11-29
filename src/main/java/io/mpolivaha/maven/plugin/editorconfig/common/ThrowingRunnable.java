package io.mpolivaha.maven.plugin.editorconfig.common;

/**
 * Runnable that can throw any {@link Throwable}
 *
 * @author Mikhail Polivakha
 */
@FunctionalInterface
public interface ThrowingRunnable<T, E extends Throwable> {

  void run() throws E;
}
