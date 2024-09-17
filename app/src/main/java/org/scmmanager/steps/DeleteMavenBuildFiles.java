/*
 * Copyright (c) 2020 - present Cloudogu GmbH
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 */

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
