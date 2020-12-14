package org.scmmanager.steps;

import org.scmmanager.Context;

import java.io.IOException;

public interface Step {

  String getName();

  void execute(Context context) throws IOException;

}
