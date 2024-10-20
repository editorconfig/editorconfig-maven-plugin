package io.mpolivaha.maven.plugin.editorconfig.config;

import java.util.EnumMap;
import java.util.Map;

public class PluginConfiguration {

  private final Map<Param, Object> parameters;

  private static PluginConfiguration INSTANCE;

  public static void buildInstance(Map<Param, Object> parameters) {
    // no need for synchronization since we are operating within single thread
    if (INSTANCE == null) {
      INSTANCE = new PluginConfiguration(parameters);
    } else {
      throw new IllegalArgumentException("PluginConfiguration instance is already initialized");
    }
  }

  public static PluginConfiguration getInstance() {
    if (INSTANCE == null) {
      throw new IllegalArgumentException("initialize PluginConfiguration before calling getInstance()");
    }
    return INSTANCE;
  }

  private PluginConfiguration(Map<Param, Object> source) {
    this.parameters = new EnumMap<>(source);
  }

  @SuppressWarnings("unchecked")
  public <T> T isStrictMode() {
    return (T) this.parameters.get(Param.STRICT_MODE);
  }

  @SuppressWarnings("unchecked")
  public <T> T getLog() {
    return (T) this.parameters.get(Param.LOG);
  }

  public enum Param {
    STRICT_MODE,
    LOG
  }
}
