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

package org.scmmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class License {

  private final List<String> content;

  public License(List<String> content) {
    this.content = content;
  }

  public List<String> getContent() {
    return content;
  }

  static License read(Path pluginDirectory) throws IOException {
    Path path = pluginDirectory.resolve("LICENSE.txt");
    List<String> content = Files.readAllLines(path);
    return new License(content);
  }
}
