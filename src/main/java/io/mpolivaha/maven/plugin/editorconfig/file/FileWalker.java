package io.mpolivaha.maven.plugin.editorconfig.file;

import io.mpolivaha.maven.plugin.editorconfig.common.ThrowingSupplier;
import io.mpolivaha.maven.plugin.editorconfig.config.PluginConfiguration;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExceptionUtils;
import io.mpolivaha.maven.plugin.editorconfig.utils.ExecutionUtils;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.maven.plugin.logging.Log;

public class FileWalker {

  private static final BiFunction<String, Exception, String> DIRECTORY_VISIT_ERROR = (path, error) ->
      "Error during walking the file-tree. Unable to fully traverse directory '%s'. Exception: \n%s".formatted(path, ExceptionUtils.getStackTrace(error));

  private static final BiFunction<String, Exception, String> FILE_INSPECTION_ERROR = (path, error) ->
      "Error during inspecting the file : '%s'. Exception: \n%s".formatted(path, ExceptionUtils.getStackTrace(error));

  public void walkRecursiveFilesInDirectory(Path root, Consumer<Path> contentConsumer) throws IOException {
    Files.walkFileTree(
        root,
        Set.of(FileVisitOption.FOLLOW_LINKS),
        Integer.MAX_VALUE,
        new FileVisitor<>() {

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) { return null; }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            try {
              contentConsumer.accept(file);
            } catch (Throwable t) {
              return FileVisitResult.TERMINATE;
            }
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            if (exc != null) {
              if (file.toFile().isDirectory()) {
                ExecutionUtils.handleError(DIRECTORY_VISIT_ERROR.apply(file.toFile().getAbsolutePath(), exc));
              } else {
                ExecutionUtils.handleError(FILE_INSPECTION_ERROR.apply(file.toFile().getAbsolutePath(), exc));
              }
            }
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc != null) {
              // This will fail the execution in case we're in strict mode
              ExecutionUtils.handleError(DIRECTORY_VISIT_ERROR.apply(dir.toFile().getAbsolutePath(), exc));
            } else {
              PluginConfiguration
                  .getInstance()
                  .<Log>getLog().debug("The directory '%s' and all recursive files have been investigated successfully");
            }
            return FileVisitResult.CONTINUE;
          }
        }
      );
  }
}
