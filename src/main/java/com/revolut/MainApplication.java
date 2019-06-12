package com.revolut;

import org.apache.log4j.Logger;

/**
 * Main class with start application point.
 */
final class MainApplication {
  /**
   * Exit status.
   */
  private static final int STATUS = -2;

  /**
   * Private constructor.
   */
  private MainApplication() {
  }

  /**
   * Start point.
   *
   * @param args - argument array.
   */
  public static void main(final String[] args) {
    Logger logger = Logger.getLogger(MainApplication.class);
    Server server = new Server();
    try {
      server.startServer();
    } catch (Exception e) {
      logger.error("Couldn't start server", e);
      System.exit(STATUS);
    }

  }
}
