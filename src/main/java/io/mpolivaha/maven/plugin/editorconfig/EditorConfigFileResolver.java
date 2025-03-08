package io.mpolivaha.maven.plugin.editorconfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.maven.project.MavenProject;

/**
 * Class that is responsible to discover the location of root .editorconfig file
 *
 * @author Mikhail Polivakha
 */
public class EditorConfigFileResolver {

  /**
   * Resolves {@code .editorconfig} file from relative path. The relative path is assumed to
   * be relative to the {@link MavenProject#getBasedir()} Maven Project basedir}
   *
   * @param editorconfig - user specified .editorconfig location
   * @return Optional Path of editorconfig root file. {@link Optional#empty() empty optional} in case file is not found
   */
  public Optional<Path> findRootEditorConfig(MavenProject mavenProject, String editorconfig) {
      File basedir = mavenProject.getBasedir();

      return Optional
        .of(Paths.get(basedir.getAbsolutePath(), editorconfig))
        .filter(Files::exists);
  }
}
