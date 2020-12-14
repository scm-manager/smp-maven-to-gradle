package org.scmmanager.steps;

import org.scmmanager.Context;
import org.scmmanager.License;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CreateSettingsGradle implements Step {
  @Override
  public String getName() {
    return "Create settings.gradle";
  }

  @Override
  public void execute(Context context) throws IOException {
    Path file = context.getDirectory().resolve("settings.gradle");
    try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
      context.getLicense().ifPresent(license(writer));
      writer.append("rootProject.name = '").append(context.getPomXml().getArtifactId()).append("'");
      writer.newLine();
    }
  }

  private Consumer<License> license(BufferedWriter writer) {
    return license -> {
      try {
        writer.append("/*");
        writer.newLine();
        for (String line : license.getContent()) {
          writer.append(" * ").append(line);
          writer.newLine();
        }
        writer.append(" */");
        writer.newLine();
        writer.newLine();
      } catch (IOException e) {
        throw new IllegalStateException("failed to write gradle.properties");
      }
    };
  }
}
