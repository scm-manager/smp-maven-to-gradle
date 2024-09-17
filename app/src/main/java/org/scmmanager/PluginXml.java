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

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.zip.ZipUtil;

import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlValue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeoutException;

@XmlRootElement(name = "plugin")
@XmlAccessorType(XmlAccessType.FIELD)
public class PluginXml {

  private Information information;
  private Conditions conditions;

  @XmlElement(name = "dependency")
  @XmlElementWrapper(name = "dependencies")
  private List<Dependency> dependencies;

  @XmlElement(name = "dependency")
  @XmlElementWrapper(name = "optional-dependencies")
  private List<Dependency> optionalDependencies;

  PluginXml() {
  }

  public Information getInformation() {
    return information;
  }

  public Conditions getConditions() {
    return conditions;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public List<Dependency> getOptionalDependencies() {
    return optionalDependencies;
  }

  static PluginXml read(Path pluginDirectory, PomXml pom) throws InterruptedException, TimeoutException, IOException {
    String smpName = String.format("%s-%s.smp", pom.getArtifactId(), pom.getVersion());
    Path smpPath = Paths.get(pluginDirectory.toString(), "target",  smpName);
    if (!Files.exists(smpPath)) {
      System.out.println("... build smp file");

      int rc = new ProcessExecutor()
        .command("./mvnw", "clean", "package", "-DskipTests")
        .directory(pluginDirectory.toFile())
        .redirectOutput(System.out)
        .execute()
        .getExitValue();

      Preconditions.checkState(rc == 0, "Failed to build plugin");
    }

    byte[] bytes = ZipUtil.unpackEntry(smpPath.toFile(), "META-INF/scm/plugin.xml");
    return JAXB.unmarshal(new ByteArrayInputStream(bytes), PluginXml.class);
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Information {

    private String displayName;
    private String description;
    private String author;
    private String category;

    public String getDisplayName() {
      return displayName;
    }

    public String getDescription() {
      return description;
    }

    public String getAuthor() {
      return author;
    }

    public String getCategory() {
      return category;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Conditions {

    @XmlElement(name = "min-version")
    private String minVersion;

    public String getMinVersion() {
      return minVersion;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Dependency {

    @XmlAttribute
    private String version;

    @XmlValue
    private String name;

    public String getName() {
      return name;
    }

    public String getVersion() {
      return version;
    }

    public String getGradleString() {
      return "sonia.scm.plugins:" + name + ":" + version;
    }
  }
}
