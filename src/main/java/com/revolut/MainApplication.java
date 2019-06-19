package com.revolut;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class with start application point.
 */
@Slf4j
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
    Server server = new Server();
    try {
      server.startServer();
    } catch (Exception e) {
      log.error("Couldn't start server", e);
      System.exit(STATUS);
    }

  }
}
