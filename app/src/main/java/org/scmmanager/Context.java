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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public final class Context {

  private final Path directory;
  private final PomXml pomXml;
  private final PluginXml pluginXml;
  private final License license;
  private final List<String> notifications = new ArrayList<>();

  private Context(Path directory, PomXml pomXml, PluginXml pluginXml, License license) {
    this.directory = directory;
    this.pomXml = pomXml;
    this.pluginXml = pluginXml;
    this.license = license;
  }

  public Path getDirectory() {
    return directory;
  }

  public PomXml getPomXml() {
    return pomXml;
  }

  public PluginXml getPluginXml() {
    return pluginXml;
  }


  public Optional<License> getLicense() {
    return Optional.ofNullable(license);
  }

  public List<String> getNotifications() {
    return notifications;
  }

  public void notify(String message) {
    notifications.add(message);
  }

  static Context create(Path directory) throws InterruptedException, IOException, TimeoutException {
    PomXml pomXml = PomXml.read(directory);
    PluginXml pluginXml = PluginXml.read(directory, pomXml);
    License license = License.read(directory);

    return new Context(directory, pomXml, pluginXml, license);
  }

}
