/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.batch.issue;

import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.component.Component;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.batch.ProjectTree;
import org.sonar.core.component.ResourceComponent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class IssuableFactoryTest {

  ModuleIssues moduleIssues = mock(ModuleIssues.class);
  IssueCache cache = mock(IssueCache.class, Mockito.RETURNS_MOCKS);
  ProjectTree projectTree = mock(ProjectTree.class);

  @Test
  public void file_should_be_issuable() {
    IssuableFactory factory = new IssuableFactory(moduleIssues, cache, projectTree);
    Component component = new ResourceComponent(File.create("foo/bar.c").setEffectiveKey("foo/bar.c"));
    Issuable issuable = factory.loadPerspective(Issuable.class, component);

    assertThat(issuable).isNotNull();
    assertThat(issuable.component()).isSameAs(component);
    assertThat(issuable.issues()).isEmpty();
  }

  @Test
  public void project_should_be_issuable() {
    IssuableFactory factory = new IssuableFactory(moduleIssues, cache, projectTree);
    Component component = new ResourceComponent(new Project("Foo").setEffectiveKey("foo"));
    Issuable issuable = factory.loadPerspective(Issuable.class, component);

    assertThat(issuable).isNotNull();
    assertThat(issuable.component()).isSameAs(component);
    assertThat(issuable.issues()).isEmpty();
  }
}
