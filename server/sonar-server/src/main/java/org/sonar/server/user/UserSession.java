package org.sonar.server.user;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public interface UserSession {
  @CheckForNull
  String login();

  @CheckForNull
  String name();

  @CheckForNull
  Integer userId();

  Set<String> userGroups();

  boolean isLoggedIn();

  Locale locale();

  /**
   * Ensures that user is logged in otherwise throws {@link org.sonar.server.exceptions.UnauthorizedException}.
   */
  UserSession checkLoggedIn();

  /**
   * Ensures that user implies the specified global permission, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException}.
   */
  UserSession checkGlobalPermission(String globalPermission);

  /**
   * Ensures that user implies the specified global permission, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException} with
   * the specified error message.
   */
  UserSession checkGlobalPermission(String globalPermission, @Nullable String errorMessage);

  /**
   * Does the user have the given permission ?
   */
  boolean hasGlobalPermission(String globalPermission);

  List<String> globalPermissions();

  /**
   * Ensures that user implies the specified project permission, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException}.
   */
  UserSession checkProjectPermission(String projectPermission, String projectKey);

  /**
   * Ensures that user implies the specified project permission, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException}.
   */
  UserSession checkProjectUuidPermission(String projectPermission, String projectUuid);

  /**
   * Does the user have the given project permission ?
   */
  boolean hasProjectPermission(String permission, String projectKey);

  /**
   * Does the user have the given project permission ?
   */
  boolean hasProjectPermissionByUuid(String permission, String projectUuid);

  /**
   * Ensures that user implies the specified project permission on a component, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException}.
   */
  UserSession checkComponentPermission(String projectPermission, String componentKey);

  /**
   * Ensures that user implies the specified component permission on a component, otherwise throws a {@link org.sonar.server.exceptions.ForbiddenException}.
   */
  UserSession checkComponentUuidPermission(String permission, String componentUuid);

  /**
   * Does the user have the given project permission for a component key ?
   */
  boolean hasComponentPermission(String permission, String componentKey);

  /**
   * Does the user have the given project permission for a component uuid ?
   */
  boolean hasComponentUuidPermission(String permission, String componentUuid);
}
