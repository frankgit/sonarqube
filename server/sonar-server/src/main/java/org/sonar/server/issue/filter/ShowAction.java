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

package org.sonar.server.issue.filter;

import com.google.common.io.Resources;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.text.JsonWriter;
import org.sonar.core.issue.db.IssueFilterDto;
import org.sonar.server.user.ThreadLocalUserSession;
import org.sonar.server.user.UserSession;

public class ShowAction implements RequestHandler {

  private final IssueFilterService service;
  private final IssueFilterWriter issueFilterWriter;

  public ShowAction(IssueFilterService service, IssueFilterWriter issueFilterWriter) {
    this.service = service;
    this.issueFilterWriter = issueFilterWriter;
  }

  void define(WebService.NewController controller) {
    WebService.NewAction action = controller.createAction("show");
    action
      .setDescription("Get detail of an issue filter. Requires to be authenticated")
      .setSince("4.2")
      .setHandler(this)
      .setResponseExample(Resources.getResource(this.getClass(), "example-show.json"));
    action.createParam("id")
      .setDescription("ID of the issue filter")
      .setRequired(true);
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    UserSession session = userSession;
    IssueFilterDto filter = service.find(request.mandatoryParamAsLong("id"), session);

    JsonWriter json = response.newJsonWriter();
    json.beginObject();
    issueFilterWriter.write(session, filter, json);
    json.endObject();
    json.close();
  }
}
