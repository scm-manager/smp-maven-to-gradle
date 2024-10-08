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

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.scmmanager.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CopyStaticFiles implements Step {

  private static final List<StaticFile> FILES = ImmutableList.of(
    new StaticFile("gitignore", ".gitignore"),
    new StaticFile("gradlew", "gradlew", true),
    new StaticFile("gradlew.bat", "gradlew.bat", true),
    new StaticFile("gradle-wrapper.jar", "gradle/wrapper/gradle-wrapper.jar"),
    new StaticFile("gradle-wrapper.properties", "gradle/wrapper/gradle-wrapper.properties")
  );

  @Override
  public String getName() {
    return "Copy static files";
  }

  @Override
  public void execute(Context context) throws IOException {
    for (StaticFile file : FILES) {
      copy(context, file);
    }
  }

  private void copy(Context context, StaticFile file) throws IOException {
    System.out.append("copy from ").append(file.source).append(" to ").println(file.target);

    URL resource = Resources.getResource("org/scmmanager/steps/" + file.source);
    Path target = context.getDirectory().resolve(file.target);
    if (!Files.exists(target.getParent())) {
      Files.createDirectories(target.getParent());
    }

    try (OutputStream output = Files.newOutputStream(target)) {
      Resources.copy(resource, output);
    }
    if (file.executable) {
      target.toFile().setExecutable(true);
    }
  }

  public static class StaticFile {

    private final String source;
    private final String target;
    private final boolean executable;

    public StaticFile(String source, String target) {
      this(source, target, false);
    }

    public StaticFile(String source, String target, boolean executable) {
      this.source = source;
      this.target = target;
      this.executable = executable;
    }
  }
}

