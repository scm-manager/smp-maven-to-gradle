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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.scmmanager.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MigratePackageJson implements Step {
  @Override
  public String getName() {
    return "migrate package.json";
  }

  @Override
  public void execute(Context context) throws IOException {
    Path path = context.getDirectory().resolve("package.json");
    if (Files.exists(path)) {
      migrate(path);
    } else {
      System.out.println("no package.json found, nothing to migrate");
    }
  }

  private void migrate(Path path) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(path.toFile());

    ObjectNode devDependencies = (ObjectNode) node.get("devDependencies");
    devDependencies.put("@scm-manager/plugin-scripts", "^1.1.0");
    if (devDependencies.has("@scm-manager/jest-preset")) {
      devDependencies.put("@scm-manager/jest-preset", "^2.12.3");
    }

    ObjectNode scripts = (ObjectNode) node.get("scripts");
    if (scripts.has("postinstall")) {
      scripts.put("postinstall", "plugin-scripts postinstall");
    }
    if (scripts.has("build")) {
      scripts.put("build", "plugin-scripts build");
    }
    if (scripts.has("watch")) {
      scripts.put("watch", "plugin-scripts watch");
    }
    if (scripts.has("deploy")) {
      scripts.put("deploy", "plugin-scripts publish");
    }

    mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), node);
  }
}
