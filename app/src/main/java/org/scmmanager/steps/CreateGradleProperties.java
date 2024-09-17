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

import org.scmmanager.Context;
import org.scmmanager.License;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CreateGradleProperties implements Step {

  @Override
  public String getName() {
    return "Create gradle.properties";
  }

  @Override
  public void execute(Context context) throws IOException {
    Path file = context.getDirectory().resolve("gradle.properties");
    try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
      context.getLicense().ifPresent(license(writer));
      writer.append("version=").append(context.getPomXml().getVersion());
      writer.newLine();
    }
  }

  private Consumer<License> license(BufferedWriter writer) {
    return license -> {
      try {
        writer.append("#");
        writer.newLine();
        for (String line : license.getContent()) {
          writer.append("# ").append(line);
          writer.newLine();
        }
        writer.append("#");
        writer.newLine();
        writer.newLine();
      } catch (IOException e) {
        throw new IllegalStateException("failed to write gradle.properties");
      }
    };
  }
}
