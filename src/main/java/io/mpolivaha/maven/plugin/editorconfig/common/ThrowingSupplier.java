package io.mpolivaha.maven.plugin.editorconfig.common;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

  T get() throws E;
}
