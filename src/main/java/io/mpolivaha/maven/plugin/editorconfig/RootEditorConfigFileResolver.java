package io.mpolivaha.maven.plugin.editorconfig;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.maven.project.MavenProject;

/**
 * Class that is responsible to discover the location of root .editorconfig file
 *
 * @author Mikhail Polivakha
 */
public class RootEditorConfigFileResolver {

  /**
   *
   * @param editorconfig - user specified .editorconfig location
   * @return Optional Path of editorconfig root file. {@link Optional#empty() empty optional} in case file is not found
   * TODO: implement
   */
  public Optional<Path> findRootEditorConfig(MavenProject mavenProject, String editorconfig) {
    throw new UnsupportedOperationException();
  }
}
