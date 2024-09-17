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

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

public class Steps implements Iterable<Step> {

  private final List<Step> steps;

  private Steps(List<Step> steps) {
    this.steps = steps;
  }

  @Override
  public Iterator<Step> iterator() {
    return steps.iterator();
  }

  public static Steps get() {
    return new Steps(ImmutableList.of(
      new CreateGradleProperties(),
      new CopyStaticFiles(),
      new CreateSettingsGradle(),
      new BuildGradle(),
      new MigratePackageJson(),
      new DeleteMavenBuildFiles()
    ));
  }
}
