package org.scmmanager.steps;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.scmmanager.Context;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DeleteMavenBuildFiles implements Step {

  private static final List<String> FILES = ImmutableList.of(
    ".mvn/wrapper/MavenWrapperDownloader.java",
    ".mvn/wrapper/maven-wrapper.properties",
    ".mvn/wrapper/maven-wrapper.jar",
    ".mvn/wrapper",
    ".mvn",
    "mvnw",
    "mvnw.cmd",
    "pom.xml",
    "src/main/resources/META-INF/scm/plugin.xml"
  );

  @Override
  public String getName() {
    return "Delete Maven build files";
  }

  @Override
  public void execute(Context context) throws IOException {
    clean(context);

    for (String relativeFilePath : FILES) {
      Path path = context.getDirectory().resolve(relativeFilePath);
      System.out.println("delete file " + path);
      Files.deleteIfExists(path);
    }
  }

  private void clean(Context context) {
    System.out.println("execute maven clean");
    int rc = 0;
    try {
      rc = new ProcessExecutor()
        .command("./mvnw", "clean")
        .directory(context.getDirectory().toFile())
        .redirectOutput(System.out)
        .execute()
        .getExitValue();

      Preconditions.checkState(rc == 0, "failed to execute maven clean");
    } catch (IOException | TimeoutException e) {
      throw new IllegalStateException("failed to execute maven clean");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("failed to execute maven clean");
    }
  }
}
