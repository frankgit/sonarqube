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
package org.sonar.server.user;

import com.google.common.base.Objects;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Part of the current HTTP session
 */
public class ThreadLocalUserSession implements UserSession {

  private static class AnonymousUserSession extends AbstractUserSession<AnonymousUserSession> {

    private AnonymousUserSession() {
      super(AnonymousUserSession.class);
    }

    @Override
    public List<String> globalPermissions() {
      return Collections.emptyList();
    }

    @Override
    public boolean hasProjectPermission(String permission, String projectKey) {
      return false;
    }

    @Override
    public boolean hasProjectPermissionByUuid(String permission, String projectUuid) {
      return false;
    }

    @Override
    public boolean hasComponentPermission(String permission, String componentKey) {
      return false;
    }

    @Override
    public boolean hasComponentUuidPermission(String permission, String componentUuid) {
      return false;
    }
  }

  public static final UserSession ANONYMOUS = new AnonymousUserSession();

  private static final ThreadLocal<UserSession> THREAD_LOCAL = new ThreadLocal<UserSession>();
  private static final Logger LOG = Loggers.get(ThreadLocalUserSession.class);

  public UserSession get() {
    return Objects.firstNonNull(THREAD_LOCAL.get(), ANONYMOUS);
  }

  public void set(UserSession session) {
    THREAD_LOCAL.set(session);
  }

  public void remove() {
    THREAD_LOCAL.remove();
  }

  public boolean hasSession() {
    return THREAD_LOCAL.get() != null;
  }

  @Override
  @CheckForNull
  public String login() {
    return get().login();
  }

  @Override
  @CheckForNull
  public String name() {
    return get().name();
  }

  @Override
  @CheckForNull
  public Integer userId() {
    return get().userId();
  }

  @Override
  public Set<String> userGroups() {
    return get().userGroups();
  }

  @Override
  public boolean isLoggedIn() {
    return get().isLoggedIn();
  }

  @Override
  public Locale locale() {
    return get().locale();
  }

  @Override
  public UserSession checkLoggedIn() {
    return get().checkLoggedIn();
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission) {
    return get().checkGlobalPermission(globalPermission);
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission, @Nullable String errorMessage) {
    return get().checkGlobalPermission(globalPermission, errorMessage);
  }

  @Override
  public boolean hasGlobalPermission(String globalPermission) {
    return get().hasGlobalPermission(globalPermission);
  }

  @Override
  public List<String> globalPermissions() {
    return get().globalPermissions();
  }

  @Override
  public UserSession checkProjectPermission(String projectPermission, String projectKey) {
    return get().checkProjectPermission(projectPermission, projectKey);
  }

  @Override
  public UserSession checkProjectUuidPermission(String projectPermission, String projectUuid) {
    return get().checkProjectUuidPermission(projectPermission, projectUuid);
  }

  @Override
  public boolean hasProjectPermission(String permission, String projectKey) {
    return get().hasProjectPermission(permission, projectKey);
  }

  @Override
  public boolean hasProjectPermissionByUuid(String permission, String projectUuid) {
    return get().hasProjectPermissionByUuid(permission, projectUuid);
  }

  @Override
  public UserSession checkComponentPermission(String projectPermission, String componentKey) {
    return get().checkComponentPermission(projectPermission, componentKey);
  }

  @Override
  public UserSession checkComponentUuidPermission(String permission, String componentUuid) {
    return get().checkComponentUuidPermission(permission, componentUuid);
  }

  @Override
  public boolean hasComponentPermission(String permission, String componentKey) {
    return get().hasComponentPermission(permission, componentKey);
  }

  @Override
  public boolean hasComponentUuidPermission(String permission, String componentUuid) {
    return get().hasComponentUuidPermission(permission, componentUuid);
  }
}
