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
