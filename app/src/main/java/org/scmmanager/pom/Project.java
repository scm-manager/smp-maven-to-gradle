package org.scmmanager.pom;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

  private String artifactId;
  private String version;
  private Build build;

  @XmlElement(name = "dependency")
  @XmlElementWrapper(name = "dependencies")
  private List<Dependency> dependencies;

  public String getArtifactId() {
    return artifactId;
  }


  public String getVersion() {
    return version;
  }


  public Build getBuild() {
    return build;
  }

  public List<Dependency> getDependencies() {
    if (dependencies == null) {
      return Collections.emptyList();
    }
    return dependencies;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Build {

    @XmlElement(name = "plugin")
    @XmlElementWrapper(name = "plugins")
    private List<Plugin> plugins;

    public List<Plugin> getPlugins() {
      return plugins;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Plugin {

    private String groupId;
    private String artifactId;
    private Configuration configuration;

    public String getArtifactId() {
      return artifactId;
    }

    public String getGroupId() {
      return groupId;
    }

    public Configuration getConfiguration() {
      return configuration;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Configuration {

    @XmlElement(name = "resourcePackage")
    @XmlElementWrapper(name = "resourcePackages")
    private List<String> resourcePackages;

    public List<String> getResourcePackages() {
      return resourcePackages;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Dependency {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public String getGroupId() {
      return groupId;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public String getVersion() {
      return version;
    }

    public String getScope() {
      return scope;
    }
  }

}
