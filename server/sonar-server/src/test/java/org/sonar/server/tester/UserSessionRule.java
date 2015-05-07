package org.sonar.server.tester;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.sonar.server.user.MockUserSession;
import org.sonar.server.user.ThreadLocalUserSession;
import org.sonar.server.user.UserSession;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class UserSessionRule implements TestRule, UserSession {

  private MockUserSession ruleUserSession = new MockUserSession();
  @CheckForNull
  private MockUserSession statementUserSession;
  @CheckForNull
  private UserSession userSession;
  @CheckForNull
  private UserSession currentUserSession;
  @CheckForNull
  private ServerTester serverTester;

  @Override
  public Statement apply(Statement statement, Description description) {
    return this.statement(statement);
  }

  private Statement statement(final Statement base) {
    return new Statement() {
      public void evaluate() throws Throwable {
        UserSessionRule.this.before();

        try {
          base.evaluate();
        } finally {
          UserSessionRule.this.after();
        }

      }
    };
  }

  protected void before() throws Throwable {
    if (userSession != null) {
      this.statementUserSession = null;
      this.currentUserSession = userSession;
    } else {
      this.statementUserSession = new MockUserSession(ruleUserSession);
      this.currentUserSession = statementUserSession;
    }
    if (serverTester != null) {
      serverTester.get(ThreadLocalUserSession.class).set(statementUserSession);
    }
  }

  protected void after() {
    this.currentUserSession = ruleUserSession;
    this.userSession = null;
    this.serverTester = null;
  }

  public void set(UserSession userSession) {
    checkState(serverTester == null, "Can set a specific session and use ServerTester at the same time");
    checkNotNull(userSession);
    this.userSession = userSession;
  }

  public void useServerTest(ServerTester serverTester) {
    checkState(userSession == null, "Can set a specific session and use ServerTester at the same time");
    checkNotNull(serverTester);
    this.serverTester = serverTester;
  }

  private void checkStatementsUserSessionCall() {
    checkState(statementUserSession != null, "rule state can not be changed if a UserSession has explicitly been provided or when using ServerTester");
  }

  public MockUserSession setGlobalPermissions(String... globalPermissions) {
    checkStatementsUserSessionCall();
    return statementUserSession.setGlobalPermissions(globalPermissions);
  }

  public MockUserSession addProjectUuidPermissions(String projectPermission, String... projectUuids) {
    checkStatementsUserSessionCall();
    return statementUserSession.addProjectUuidPermissions(projectPermission, projectUuids);
  }

  @Deprecated
  public MockUserSession addComponentPermission(String projectPermission, String projectKey, String componentKey) {
    checkStatementsUserSessionCall();
    return statementUserSession.addComponentPermission(projectPermission, projectKey, componentKey);
  }

  public MockUserSession setLogin(@Nullable String s) {
    checkStatementsUserSessionCall();
    return statementUserSession.setLogin(s);
  }

  @Deprecated
  public MockUserSession addProjectPermissions(String projectPermission, String... projectKeys) {
    checkStatementsUserSessionCall();
    return statementUserSession.addProjectPermissions(projectPermission, projectKeys);
  }

  public MockUserSession setUserId(@Nullable Integer userId) {
    checkStatementsUserSessionCall();
    return statementUserSession.setUserId(userId);
  }

  public MockUserSession setUserGroups(@Nullable String... userGroups) {
    checkStatementsUserSessionCall();
    return statementUserSession.setUserGroups(userGroups);
  }

  public MockUserSession setLocale(@Nullable Locale l) {
    checkStatementsUserSessionCall();
    return statementUserSession.setLocale(l);
  }

  public MockUserSession addComponentUuidPermission(String projectPermission, String projectUuid, String componentUuid) {
    checkStatementsUserSessionCall();
    return statementUserSession.addComponentUuidPermission(projectPermission, projectUuid, componentUuid);
  }

  public MockUserSession setName(@Nullable String s) {
    checkStatementsUserSessionCall();
    return statementUserSession.setName(s);
  }

  @Override
  public List<String> globalPermissions() {
    return currentUserSession.globalPermissions();
  }

  @Override
  public boolean hasProjectPermission(String permission, String projectKey) {
    return currentUserSession.hasProjectPermission(permission, projectKey);
  }

  @Override
  public boolean hasProjectPermissionByUuid(String permission, String projectUuid) {
    return currentUserSession.hasProjectPermissionByUuid(permission, projectUuid);
  }

  @Override
  public boolean hasComponentPermission(String permission, String componentKey) {
    return currentUserSession.hasComponentPermission(permission, componentKey);
  }

  @Override
  public boolean hasComponentUuidPermission(String permission, String componentUuid) {
    return currentUserSession.hasComponentUuidPermission(permission, componentUuid);
  }

  @Override
  @CheckForNull
  public String login() {
    return currentUserSession.login();
  }

  @Override
  @CheckForNull
  public String name() {
    return currentUserSession.name();
  }

  @Override
  @CheckForNull
  public Integer userId() {
    return currentUserSession.userId();
  }

  @Override
  public Set<String> userGroups() {
    return currentUserSession.userGroups();
  }

  @Override
  public boolean isLoggedIn() {
    return currentUserSession.isLoggedIn();
  }

  @Override
  public Locale locale() {
    return currentUserSession.locale();
  }

  @Override
  public UserSession checkLoggedIn() {
    return currentUserSession.checkLoggedIn();
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission) {
    return currentUserSession.checkGlobalPermission(globalPermission);
  }

  @Override
  public UserSession checkGlobalPermission(String globalPermission, @Nullable String errorMessage) {
    return currentUserSession.checkGlobalPermission(globalPermission, errorMessage);
  }

  @Override
  public boolean hasGlobalPermission(String globalPermission) {
    return currentUserSession.hasGlobalPermission(globalPermission);
  }

  @Override
  public UserSession checkProjectPermission(String projectPermission, String projectKey) {
    return currentUserSession.checkProjectPermission(projectPermission, projectKey);
  }

  @Override
  public UserSession checkProjectUuidPermission(String projectPermission, String projectUuid) {
    return currentUserSession.checkProjectUuidPermission(projectPermission, projectUuid);
  }

  @Override
  public UserSession checkComponentPermission(String projectPermission, String componentKey) {
    return currentUserSession.checkComponentPermission(projectPermission, componentKey);
  }

  @Override
  public UserSession checkComponentUuidPermission(String permission, String componentUuid) {
    return currentUserSession.checkComponentUuidPermission(permission, componentUuid);
  }

}
