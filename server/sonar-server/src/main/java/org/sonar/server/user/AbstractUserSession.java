package org.sonar.server.user;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.sonar.server.exceptions.ForbiddenException;
import org.sonar.server.exceptions.UnauthorizedException;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public abstract class AbstractUserSession<T extends AbstractUserSession> implements UserSession {
  protected static final String INSUFFICIENT_PRIVILEGES_MESSAGE = "Insufficient privileges";

  protected Integer userId;
  protected String login;
  protected Set<String> userGroups;
  protected List<String> globalPermissions = null;
  protected HashMultimap<String, String> projectKeyByPermission = HashMultimap.create();
  protected HashMultimap<String, String> projectUuidByPermission = HashMultimap.create();
  protected Map<String, String> projectUuidByComponentUuid = newHashMap();
  protected List<String> projectPermissions = newArrayList();
  protected String name;
  protected Locale locale = Locale.ENGLISH;

  private final Class<T> clazz;

  protected AbstractUserSession(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  @CheckForNull
  public String login() {
    return login;
  }

  T setLogin(@Nullable String s) {
    this.login = Strings.emptyToNull(s);
    return clazz.cast(this);
  }

  @Override
  @CheckForNull
  public String name() {
    return name;
  }

  T setName(@Nullable String s) {
    this.name = Strings.emptyToNull(s);
    return clazz.cast(this);
  }

  @Override
  @CheckForNull
  public Integer userId() {
    return userId;
  }

  T setUserId(@Nullable Integer userId) {
    this.userId = userId;
    return clazz.cast(this);
  }

  @Override
  public Set<String> userGroups() {
    return userGroups;
  }

  T setUserGroups(@Nullable String... userGroups) {
    if (userGroups != null) {
      this.userGroups.addAll(Arrays.asList(userGroups));
    }
    return clazz.cast(this);
  }

  @Override
  public boolean isLoggedIn() {
    return login != null;
  }

  @Override
  public Locale locale() {
    return locale;
  }

  T setLocale(@Nullable Locale l) {
    this.locale = Objects.firstNonNull(l, Locale.ENGLISH);
    return clazz.cast(this);
  }

  @Override
  public UserSession checkLoggedIn() {
    if (login == null) {
      throw new UnauthorizedException("Authentication is required");
    }
    return this;
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission) {
    return checkGlobalPermission(globalPermission, null);
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission, @Nullable String errorMessage) {
    if (!hasGlobalPermission(globalPermission)) {
      throw new ForbiddenException(errorMessage != null ? errorMessage : INSUFFICIENT_PRIVILEGES_MESSAGE);
    }
    return this;
  }

  @Override
  public boolean hasGlobalPermission(String globalPermission) {
    return globalPermissions().contains(globalPermission);
  }

  @Override
  public UserSession checkProjectPermission(String projectPermission, String projectKey) {
    if (!hasProjectPermission(projectPermission, projectKey)) {
      throw new ForbiddenException(INSUFFICIENT_PRIVILEGES_MESSAGE);
    }
    return this;
  }

  @Override
  public UserSession checkProjectUuidPermission(String projectPermission, String projectUuid) {
    if (!hasProjectPermissionByUuid(projectPermission, projectUuid)) {
      throw new ForbiddenException(INSUFFICIENT_PRIVILEGES_MESSAGE);
    }
    return this;
  }

  @Override
  public UserSession checkComponentPermission(String projectPermission, String componentKey) {
    if (!hasComponentPermission(projectPermission, componentKey)) {
      throw new ForbiddenException(INSUFFICIENT_PRIVILEGES_MESSAGE);
    }
    return this;
  }

  @Override
  public UserSession checkComponentUuidPermission(String permission, String componentUuid) {
    if (!hasComponentUuidPermission(permission, componentUuid)) {
      throw new ForbiddenException(INSUFFICIENT_PRIVILEGES_MESSAGE);
    }
    return this;
  }
}
