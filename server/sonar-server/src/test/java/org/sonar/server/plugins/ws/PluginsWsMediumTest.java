package org.sonar.server.plugins.ws;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonar.server.tester.ServerTester;
import org.sonar.server.ws.WsTester;

public class PluginsWsMediumTest {
  @ClassRule
  public static ServerTester serverTester = new ServerTester()
    .addPluginJar(getFile("sonar-decoy-plugin-1.0.jar"))
    .setUpdateCenterUrl(getFileUrl("update-center.properties"));
  private static WsTester wsTester;

  @BeforeClass
  public static void setUp() throws Exception {
    wsTester = new WsTester(serverTester.get(PluginsWs.class));
  }

  @Test
  public void test_update_existing_and_install_new_scenario() throws Exception {
    wsTester.newGetRequest("api/plugins", "installed").execute().assertJson("{" +
      "  \"plugins\": [" +
      "    {" +
      "      \"key\": \"decoy\"," +
      "      \"version\": \"1.0\"" +
      "    }" +
      "  ]" +
      "}"
      );

    wsTester.newGetRequest("api/plugins", "available").execute().assertJson("{" +
      "  \"plugins\": [" +
      "    {" +
      "      \"key\": \"foo\"," +
      "      \"release\": {" +
      "        \"version\": \"1.0\"," +
      "      }," +
      "      \"update\": {" +
      "        \"status\": \"COMPATIBLE\"," +
      "        \"requires\": []" +
      "      }" +
      "    }" +
      "  ]" +
      "}");

    wsTester.newPostRequest("api/plugins", "update").setParam("key", "decoy").execute().assertNoContent();

    wsTester.newGetRequest("api/plugins", "pending").execute().outputAsString();

    wsTester.newPostRequest("api/plugins", "install").setParam("key", "foo").execute().assertNoContent();

    wsTester.newGetRequest("api/plugins", "pending").execute().outputAsString();

    serverTester.stop();
    serverTester.start();

    wsTester.newGetRequest("api/plugins", "installed").execute().assertJson("{" +
      "  \"plugins\": [" +
      "    {" +
      "      \"key\": \"decoy\"," +
      "      \"version\": \"1.1\"" +
      "    }," +
      "    {" +
      "      \"key\": \"foo\"," +
      "      \"version\": \"1.0\"" +
      "    }" +
      "  ]" +
      "}"
      );
  }

  private static File getFile(String jarFileName) {
    try {
      return new File(getFileUrl(jarFileName).toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static URL getFileUrl(String fileName) {
    return PluginsWsMediumTest.class.getResource(PluginsWsMediumTest.class.getSimpleName() + "/" + fileName);
  }
}
