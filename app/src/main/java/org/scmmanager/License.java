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
