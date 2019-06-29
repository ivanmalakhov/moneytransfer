package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.awaitInitialization;

@Slf4j
public class TestServerUserCreate {
  private final Server server = new Server();

  /**
   * Start server and awaiting initialization
   */
  @Before
  public void setUp() {
    server.startServer(4570);

    awaitInitialization();
  }

  /**
   * Create User
   */
  @Test
  public void testUserCreate() {
    Gson gson = new Gson();
    String body = "{\n" +
            "  \"firstName\" : \"FirstName\",\n" +
            "  \"secondName\" : \"LastName\"\n" +
            "}";
    ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", 4570, "/users", body);
    assertNotNull(res);
    log.info("Create user response: {}", res.body);
    assertEquals(201, res.status);
    String userId = gson.fromJson(res.body, JsonObject.class)
            .getAsJsonObject("Info")
            .getAsJsonObject("User")
            .get("id").toString();


    res = ApiTestUtils.request("GET", 4570, "/users", null);
    assertNotNull(res);
    log.info("Get users response: {}", res.body);
    assertEquals(201, res.status);

    res = ApiTestUtils.request("GET",
                               4570,
                               "/users/" + userId,
                               null);
    assertNotNull(res);
    log.info("Get users response: {}", res.body);
    assertEquals(201, res.status);
  }

  /**
   * Stop server after all test
   */
  @After
  public void stop() {
    server.stopServer();
  }
}
