package com.revolute;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static spark.Spark.awaitInitialization;

@Ignore
public class TestServerAlive {
  private Logger logger = Logger.getLogger(TestServerAlive.class);
  private Server server = new Server();

  /**
   * Start server and awaiting initialization
   */
  @Before
  public void setUp() {
    server.startServer(4568);

    awaitInitialization();
  }

  /**
   * Alive server test
   */
  @Test
  public void testAlive() {
    ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", 4568, "/alive", null);
    assertNotNull(res);
    assertEquals(200, res.status);
    assertEquals("ok", res.body);
  }

  /**
   * Stop server after all test
   */
  @After
  public void stop() {
    server.stopServer();

  }
}
