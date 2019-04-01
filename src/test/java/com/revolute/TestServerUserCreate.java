package com.revolute;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static spark.Spark.awaitInitialization;

public class TestServerUserCreate {
  private Logger logger = Logger.getLogger(TestServerAlive.class);
  private Server server = new Server();

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
    String body = "{\n" +
            "  \"firstName\" : \"Ivanov\",\n" +
            "  \"secondName\" : \"Ivan\"\n" +
            "}";
    ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", 4570, "/user/create", body);
    assertNotNull(res);
    logger.info(res.body);
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
