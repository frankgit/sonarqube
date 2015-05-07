package org.sonar.org.sonar.plugins.foo;

import java.util.Collections;
import java.util.List;
import org.sonar.api.Plugin;

public class FooPlugin implements Plugin {
  @Override
  public String getKey() {
    return "foo";
  }

  @Override
  public String getName() {
    return "foo";
  }

  @Override
  public String getDescription() {
    return "foo";
  }

  @Override
  public List getExtensions() {
    return Collections.emptyList();
  }
}
