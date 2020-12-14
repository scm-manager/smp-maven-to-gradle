package org.scmmanager.steps;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.scmmanager.Context;
import org.scmmanager.License;
import org.scmmanager.PluginXml;
import org.scmmanager.PomXml;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BuildGradle implements Step {
  @Override
  public String getName() {
    return "Create build.gradle file";
  }

  @Override
  public void execute(Context context) throws IOException {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile("org/scmmanager/steps/build.gradle.mustache");

    Path path = context.getDirectory().resolve("build.gradle");
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      mustache.execute(writer, new Model(context));
    }

    List<String> dependencies = new ArrayList<>();
    dependencies.addAll(context.getPomXml().getRuntimeDependencies());
    dependencies.addAll(context.getPomXml().getTestDependencies());

    List<String> unresolvedRuntime = dependencies.stream()
      .filter(f -> f.contains("{")).collect(Collectors.toList());

    if (!unresolvedRuntime.isEmpty()) {
      StringBuilder message = new StringBuilder();
      message.append("!!! WARNING !!!\n");
      message.append("Some of the dependencies contains unresolved variables\n");
      message.append("Those variable must be resolved by hand\n");
      message.append("\n");
      for (String dep : unresolvedRuntime) {
        message.append(" - ").append(dep).append("\n");
      }
      message.append("\n");
      message.append("!!! WARNING !!!");
      context.notify(message.toString());
    }
  }


  public static class Model {

    private final Context context;
    private final List<String> license;

    public Model(Context context) {
      this.context = context;
      this.license = context.getLicense().map(License::getContent).orElse(Collections.emptyList());
    }

    public List<String> getLicense() {
      return license;
    }

    public PluginXml getPluginXml() {
      return context.getPluginXml();
    }

    public PomXml getPomXml() {
      return context.getPomXml();
    }
  }
}
