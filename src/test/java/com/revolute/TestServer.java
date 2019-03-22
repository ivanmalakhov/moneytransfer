package com.revolute;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitInitialization;

public class TestServer {
  private Logger logger = Logger.getLogger(TestServer.class);
  private Server server = new Server();

  /**
   * Start server and awaiting initialization
   */
  @Before
  public void setUp(){
    server.startServer(4568);

    awaitInitialization();
  }

  /**
   * Alive server test
   */
  @Test
  public void testAlive() {
    ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", 4568, "/alive", null);
    assertEquals(200, res.status);
    assertEquals("ok",res.body);
  }

  /**
   * Stop server after all test
   */
  @After
  public void stop() {
    server.stop();
  }
}
