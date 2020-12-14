package org.scmmanager;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import jakarta.xml.bind.JAXB;
import org.scmmanager.pom.Project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PomXml {

  private final Project project;

  private PomXml(Project project) {
    this.project = project;
  }

  static PomXml read(Path pluginDirectory) {
    Path path = pluginDirectory.resolve("pom.xml");
    Preconditions.checkState(Files.exists(path), "could not find pom.xml");
    Project project = JAXB.unmarshal(path.toFile(), Project.class);
    return new PomXml(project);
  }

  public String getArtifactId() {
    return project.getArtifactId();
  }

  public String getVersion() {
    return project.getVersion();
  }

  public List<String> getOpenApiPackages() {
    Optional<Project.Plugin> apiPlugin = findOpenApiPlugin();
    return apiPlugin.map(plugin -> {
      if (plugin.getConfiguration() != null) {
        return plugin.getConfiguration().getResourcePackages();
      }
      return Collections.<String>emptyList();
    }).orElse(Collections.emptyList());
  }

  public List<String> getRuntimeDependencies() {
    return project.getDependencies().stream()
      .filter(dep -> dep.getScope() == null || "runtime".equals(dep.getScope()))
      .filter(dep -> !(dep.getArtifactId().startsWith("scm-") && dep.getArtifactId().endsWith("-plugin")))
      .map(dep -> Joiner.on(":").join(dep.getGroupId(), dep.getArtifactId(), dep.getVersion()))
      .collect(Collectors.toList());
  }

  public List<String> getTestDependencies() {
    return project.getDependencies().stream()
      .filter(dep -> "test".equals(dep.getScope()))
      .filter(dep -> !dep.getGroupId().equals("org.mockito"))
      .map(dep -> Joiner.on(":").join(dep.getGroupId(), dep.getArtifactId(), dep.getVersion()))
      .collect(Collectors.toList());
  }

  private Optional<Project.Plugin> findOpenApiPlugin() {
    if (project.getBuild() != null && project.getBuild().getPlugins() != null) {
      return project.getBuild().getPlugins().stream()
        .filter(plugin -> "io.openapitools.swagger".equals(plugin.getGroupId()) && "swagger-maven-plugin".equals(plugin.getArtifactId()))
        .findFirst();
    }
    return Optional.empty();
  }

}
