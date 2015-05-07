package org.sonar.plugins.decoy;

import java.util.Collections;
import java.util.List;
import org.sonar.api.Plugin;

public class DecoyPlugin implements Plugin {
  @Override
  public String getKey() {
    return "decoy";
  }

  @Override
  public String getName() {
    return "decoy";
  }

  @Override
  public String getDescription() {
    return "decoy";
  }

  @Override
  public List getExtensions() {
    return Collections.emptyList();
  }
}
