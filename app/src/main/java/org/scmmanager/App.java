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

import org.scmmanager.steps.Step;
import org.scmmanager.steps.Steps;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class App {

  public void migrate(Path pluginDirectory) throws InterruptedException, TimeoutException, IOException {
    Context context = Context.create(pluginDirectory);
    for (Step step : Steps.get()) {
      System.out.println("execute step " + step.getName());
      step.execute(context);
    }
    List<String> notifications = context.getNotifications();
    if (!notifications.isEmpty()) {
      System.out.println();
      System.out.println();
      notifications.forEach(System.out::println);
    }
  }

  public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
    String path = ".";
    if (args.length > 0) {
      path = args[0];
    }

    App app = new App();
    app.migrate(Paths.get(path));
  }
}
